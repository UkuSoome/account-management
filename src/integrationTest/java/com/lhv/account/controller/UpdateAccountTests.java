package com.lhv.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhv.account.config.TestJacksonConfig;
import com.lhv.account.dto.AccountResponse;
import com.lhv.account.dto.CreateAccountRequest;
import com.lhv.account.dto.ErrorResponse;
import com.lhv.account.dto.UpdateAccountRequest;
import com.lhv.account.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.lhv.account.util.AccountTestUtils.createTestAccount;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Import(TestJacksonConfig.class)
public class UpdateAccountTests extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private WebTestClient client;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup(@LocalServerPort int port) {
        jdbcTemplate.execute("TRUNCATE TABLE account");
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldUpdateAccountSuccessfully() {
        AccountResponse created = createTestAccount(client,"Alice", "+37251234567");
        Long accountId = created.getId();

        UpdateAccountRequest request = new UpdateAccountRequest();
        String updatedName = "Updated Name";
        String updatedNr = "+37251234568";
        request.setName(updatedName);
        request.setPhoneNr(updatedNr);

        AccountResponse response = client
                .put()
                .uri("/api/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(accountId, response.getId());
        Assertions.assertEquals(updatedName, response.getName());
        Assertions.assertEquals(updatedNr, response.getPhoneNr());
    }

    @Test
    void shouldUpdateWithPartialFields() {
        AccountResponse created = createTestAccount(client,"Alice", "+37251234567");
        Long accountId = created.getId();

        UpdateAccountRequest request = new UpdateAccountRequest();
        String updatedName = "Partial Update";
        request.setName(updatedName);

        AccountResponse response = client
                .put()
                .uri("/api/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(accountId, response.getId());
        Assertions.assertEquals(updatedName, response.getName());
    }

    @Test
    void shouldCreateAccountAndFailToUpdateNonExistingAccount() throws Exception {
        AccountResponse created = createTestAccount(client,"Alice", "+37251234567");
        Long accountId = created.getId();

        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setName("Bob");
        updateRequest.setPhoneNr("+37251234568");

        String errorBody = client
                .put()
                .uri("/api/accounts/{id}", 99999)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorBody);
        ErrorResponse errorResponse = mapper.readValue(errorBody, ErrorResponse.class);

        Assertions.assertTrue(errorResponse.getMessage().toLowerCase().contains("not found"));
    }

    @Test
    void shouldFailWhenUpdateHasNoFields() throws Exception {
        AccountResponse created = createTestAccount(client, "Alice", "+37251234567");

        UpdateAccountRequest request = new UpdateAccountRequest();

        String response = client
                .put()
                .uri("/api/accounts/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(response);
        ErrorResponse error = mapper.readValue(response, ErrorResponse.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatus());
        Assertions.assertTrue(error.getMessage().contains("At least one field must be provided"));
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() throws Exception {
        Long accountId = 1L;

        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setPhoneNr("invalid-phone");

        String errorBody = client
                .put()
                .uri("/api/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorBody);

        ErrorResponse errorResponse = mapper.readValue(errorBody, ErrorResponse.class);

        Assertions.assertTrue(errorResponse.getMessage().contains("Invalid phone number"));
    }

    @Test
    void shouldFailWhenNameTooLong() throws Exception {
        Long accountId = 1L;

        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setName("A".repeat(150));

        String errorBody = client
                .put()
                .uri("/api/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorBody);

        ErrorResponse errorResponse = mapper.readValue(errorBody, ErrorResponse.class);

        Assertions.assertTrue(errorResponse.getMessage().contains("Name cannot exceed 100 characters"));
    }
}

