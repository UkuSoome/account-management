package com.lhv.account.dto;

import com.lhv.account.entity.Account;
import com.lhv.account.model.Action;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AccountResponse {
    private Action action;
    private Long id;
    private String name;
    private String phoneNr;
    private LocalDateTime createdDtime;
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
