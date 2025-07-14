package com.lhv.account.controller;

import com.lhv.account.dto.*;
import com.lhv.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operations related to accounts")
public class AccountController {
    private final AccountService accountService;

    @Operation(
            summary = "Create a new account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = {
                            @Content(schema = @Schema(implementation = CreateAccountRequest.class))
                    }
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account created successfully",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping(value = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> createAccount(
            @Validated @RequestBody CreateAccountRequest accountRequest) {
        AccountResponse response = accountService.createAccount(accountRequest);
        return Response.ok(response);
    }

    @Operation(
            summary = "Update an existing account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = {
                            @Content(schema = @Schema(implementation = UpdateAccountRequest.class))
                    }
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account updated successfully",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PutMapping(value = "/accounts/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @Validated @RequestBody UpdateAccountRequest updateRequest) {
        AccountResponse response = accountService.updateAccount(id, updateRequest);
        return Response.ok(response);
    }

    @Operation(
            summary = "Get an account by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account found",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id) {
        AccountResponse account = accountService.findAccount(id);
        return Response.ok(account);
    }

    @Operation(
            summary = "Delete an account by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account deleted successfully",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @DeleteMapping(value = "/accounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> deleteAccountById(
            @PathVariable Long id) {
        AccountResponse account = accountService.deleteAccount(id);
        return Response.ok(account);
    }
}

