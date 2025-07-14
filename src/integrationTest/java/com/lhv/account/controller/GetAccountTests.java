package com.lhv.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhv.account.config.TestJacksonConfig;
import com.lhv.account.dto.AccountResponse;
import com.lhv.account.dto.ErrorResponse;
import com.lhv.account.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.lhv.account.util.AccountTestUtils.createTestAccount;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Import(TestJacksonConfig.class)
public class GetAccountTests extends AbstractIntegrationTest {
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
    void shouldFindExistingAccount() {
        AccountResponse created = createTestAccount(client,"Alice", "+37251234567");

        AccountResponse found = client
                .get()
                .uri("/account/{id}", created.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(found);
        Assertions.assertEquals(created.getId(), found.getId());
        Assertions.assertEquals(created.getName(), found.getName());
        Assertions.assertEquals(created.getPhoneNr(), found.getPhoneNr());
    }

    @Test
    void shouldFailToFindNonExistingAccount() throws Exception {
        String errorBody = client
                .get()
                .uri("/account/{id}", 99999)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorBody);

        ErrorResponse errorResponse = mapper.readValue(errorBody, ErrorResponse.class);
        Assertions.assertTrue(errorResponse.getMessage().contains("Account not found"));
    }
}
