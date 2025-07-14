package com.lhv.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhv.account.config.TestJacksonConfig;
import com.lhv.account.dto.AccountResponse;
import com.lhv.account.dto.CreateAccountRequest;
import com.lhv.account.dto.ErrorResponse;
import com.lhv.account.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.lhv.account.util.AccountTestUtils.createTestAccount;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Import(TestJacksonConfig.class)
public class CreateAccountTests extends AbstractIntegrationTest {

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
    void shouldCreateAccountSuccessfully() {
        String name = "Alice";
        String phoneNumber = "+37251234567";

        AccountResponse response = createTestAccount(client,name, phoneNumber);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals(name, response.getName());
        Assertions.assertEquals(phoneNumber, response.getPhoneNr());
    }

    @Test
    void shouldFailWhenNameIsBlank() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setName("");
        request.setPhoneNr("+37251234567");

        String errorBody = client
                .post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorBody);

        ErrorResponse errorResponse = mapper.readValue(errorBody, ErrorResponse.class);

        Assertions.assertTrue(errorResponse.getMessage().contains("Name is required"));
    }

    @Test
    void shouldFailWhenPhoneIsTooLong() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setName("John");
        request.setPhoneNr("1".repeat(60));

        String errorBody = client
                .post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorBody);

        ErrorResponse errorResponse = mapper.readValue(errorBody, ErrorResponse.class);

        Assertions.assertTrue(errorResponse.getMessage().contains("Phone number cannot exceed 50 characters"));
    }
}

