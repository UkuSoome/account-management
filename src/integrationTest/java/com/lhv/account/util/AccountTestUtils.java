package com.lhv.account.util;

import com.lhv.account.dto.AccountResponse;
import com.lhv.account.dto.CreateAccountRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class AccountTestUtils {
    public static AccountResponse createTestAccount(WebTestClient client, String name, String phoneNumber) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setName(name);
        request.setPhoneNr(phoneNumber);

        return client
                .post()
                .uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountResponse.class)
                .returnResult()
                .getResponseBody();
    }
}
