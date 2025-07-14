package com.lhv.account.service;

import com.lhv.account.dto.AccountResponse;
import com.lhv.account.dto.CreateAccountRequest;
import com.lhv.account.dto.UpdateAccountRequest;
import com.lhv.account.entity.Account;
import com.lhv.account.exception.AccountNotFoundException;
import com.lhv.account.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldSaveAndReturnAccountResponse() {
        String name = "Alice";
        String phone = "+37251234567";

        CreateAccountRequest request = new CreateAccountRequest();
        request.setName(name);
        request.setPhoneNr(phone);

        Account savedAccount = new Account();
        savedAccount.setId(1L);
        savedAccount.setName(name);
        savedAccount.setPhoneNr(phone);

        when(accountRepository.save(any())).thenReturn(savedAccount);

        AccountResponse response = accountService.createAccount(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals(name, response.getName());
        Assertions.assertEquals(phone, response.getPhoneNr());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        Account accountPassed = captor.getValue();
        Assertions.assertEquals(name, accountPassed.getName());
        Assertions.assertEquals(phone, accountPassed.getPhoneNr());
    }

    @Test
    void findAccount_shouldReturnAccountResponse_whenAccountExists() {
        Long id = 1L;
        Account account = new Account();
        account.setId(id);
        account.setName("Bob");
        account.setPhoneNr("+1234567890");

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        AccountResponse response = accountService.findAccount(id);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals("Bob", response.getName());
        Assertions.assertEquals("+1234567890", response.getPhoneNr());
    }

    @Test
    void findAccount_shouldThrowException_whenAccountNotFound() {
        Long id = 42L;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> {
            accountService.findAccount(id);
        });
        Assertions.assertTrue(ex.getMessage().contains("Account not found with id " + id));
    }

    @Test
    void updateAccount_shouldUpdateAndReturnAccountResponse_whenAccountExists() {
        Long id = 1L;
        Account existing = new Account();
        existing.setId(id);
        existing.setName("Old Name");
        existing.setPhoneNr("+1111111111");

        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setName("New Name");
        updateRequest.setPhoneNr("+2222222222");

        Account updated = new Account();
        updated.setId(id);
        updated.setName("New Name");
        updated.setPhoneNr("+2222222222");

        when(accountRepository.findById(id)).thenReturn(Optional.of(existing));
        when(accountRepository.save(existing)).thenReturn(updated);

        AccountResponse response = accountService.updateAccount(id, updateRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals("New Name", response.getName());
        Assertions.assertEquals("+2222222222", response.getPhoneNr());

        verify(accountRepository).save(existing);
    }

    @Test
    void updateAccount_shouldThrowException_whenAccountNotFound() {
        Long id = 99L;
        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setName("Name");

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> {
            accountService.updateAccount(id, request);
        });
        Assertions.assertTrue(ex.getMessage().contains("Account not found with id " + id));
    }

    @Test
    void updateAccount_shouldThrowIllegalArgumentException_whenUpdateRequestHasNoFields() {
        UpdateAccountRequest request = new UpdateAccountRequest();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                accountService.updateAccount(1L, request)
        );

        Assertions.assertEquals("At least one field must be provided for update.", exception.getMessage());
    }

    @Test
    void deleteAccount_shouldDeleteAndReturnAccountResponse_whenAccountExists() {
        Long id = 1L;
        Account existing = new Account();
        existing.setId(id);
        existing.setName("ToDelete");
        existing.setPhoneNr("+999999999");

        when(accountRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(accountRepository).deleteById(id);

        AccountResponse response = accountService.deleteAccount(id);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(id, response.getId());
        Assertions.assertEquals("ToDelete", response.getName());

        verify(accountRepository).deleteById(id);
    }

    @Test
    void deleteAccount_shouldThrowException_whenAccountNotFound() {
        Long id = 123L;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> {
            accountService.deleteAccount(id);
        });
        Assertions.assertTrue(ex.getMessage().contains("Account not found with id " + id));
    }
}
