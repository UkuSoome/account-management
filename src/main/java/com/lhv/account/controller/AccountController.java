package com.lhv.account.controller;

import com.lhv.account.dto.AccountResponse;
import com.lhv.account.dto.CreateAccountRequest;
import com.lhv.account.dto.Response;
import com.lhv.account.dto.UpdateAccountRequest;
import com.lhv.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> createAccount(
            @Validated @RequestBody CreateAccountRequest accountRequest) {
        AccountResponse response = accountService.createAccount(accountRequest);
        return Response.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @Validated @RequestBody UpdateAccountRequest updateRequest) {
        AccountResponse response = accountService.updateAccount(id, updateRequest);
        return Response.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id) {
        AccountResponse account = accountService.findAccount(id);
        return Response.ok(account);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> deleteAccountById(
            @PathVariable Long id) {
        AccountResponse account = accountService.deleteAccount(id);
        return Response.ok(account);
    }
}

