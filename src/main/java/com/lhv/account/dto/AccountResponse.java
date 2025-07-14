package com.lhv.account.dto;

import com.lhv.account.entity.Account;
import com.lhv.account.model.Action;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "Response object for Account operations")
public class AccountResponse {
    @Schema(description = "The action performed on the account", example = "CREATED")
    private Action action;
    @Schema(description = "Account unique identifier", example = "123")
    private Long id;
    @Schema(description = "Name of the account holder", example = "Alice")
    private String name;
    @Schema(description = "Phone number associated with the account", example = "+1234567890")
    private String phoneNr;
    @Schema(description = "Date and time when the account was created", example = "2025-07-14T12:00:00")
    private LocalDateTime createdDtime;
    @Schema(description = "Date and time when the account was last modified", example = "2025-07-15T15:30:00")
    private LocalDateTime modifiedDtime;

    public static AccountResponse fromAccount(Action action, Account account) {
        return new AccountResponse(
                action,
                account.getId(),
                account.getName(),
                account.getPhoneNr(),
                account.getCreatedDtime(),
                account.getModifiedDtime()
        );
    }
}
