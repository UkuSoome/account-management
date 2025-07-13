package com.lhv.account.service;

import com.lhv.account.dto.AccountResponse;
import com.lhv.account.dto.CreateAccountRequest;
import com.lhv.account.dto.UpdateAccountRequest;
import com.lhv.account.entity.Account;
import com.lhv.account.exception.AccountNotFoundException;
import com.lhv.account.model.Action;
import com.lhv.account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.lhv.account.dto.AccountResponse.fromAccount;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountResponse createAccount(CreateAccountRequest accountRequest) {
        Account account = new Account();
        account.setName(accountRequest.getName());
        account.setPhoneNr(accountRequest.getPhoneNr());

        Account saved = accountRepository.save(account);

        return fromAccount(Action.CREATED, saved);
    }

    public AccountResponse updateAccount(Long id, UpdateAccountRequest updateRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + id));

        account.setName(updateRequest.getName());
        account.setPhoneNr(updateRequest.getPhoneNr());

        Account saved = accountRepository.save(account);

        return AccountResponse.fromAccount(Action.UPDATED, saved);
    }

    public AccountResponse findAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + id));
        return fromAccount(Action.FOUND, account);
    }

    public AccountResponse deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + id));
        accountRepository.deleteById(id);
        return fromAccount(Action.DELETED, account);
    }
}
