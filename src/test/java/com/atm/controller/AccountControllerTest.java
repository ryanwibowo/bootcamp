package com.atm.controller;

import com.atm.model.Account;
import com.atm.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void dumpAccountFromFileAndSaveToDatabase_shouldReturnAccounts() {
        String file = "accounts.csv";

        Account acc1 = new Account();
        acc1.setAccountNumber("123456");
        acc1.setName("John Doe");

        Account acc2 = new Account();
        acc2.setAccountNumber("654321");
        acc2.setName("Jane Doe");

        List<Account> mockAccounts = List.of(acc1, acc2);

        when(accountService.dumpAccountFromFileAndSaveToDatabase(file))
                .thenReturn(mockAccounts);

        ResponseEntity<List<Account>> response =
                accountController.dumpAccountFromFileAndSaveToDatabase(file);

        assertThat(response.getBody()).containsExactly(acc1, acc2);
        verify(accountService).dumpAccountFromFileAndSaveToDatabase(file);
    }

    @Test
    void dumpAccountFromFileAndSaveToDatabase_shouldThrowException_whenServiceFails() {
        String file = "invalid.csv";

        when(accountService.dumpAccountFromFileAndSaveToDatabase(file))
                .thenThrow(new RuntimeException("File not found"));

        assertThatThrownBy(() -> accountController.dumpAccountFromFileAndSaveToDatabase(file))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("File not found");
        verify(accountService).dumpAccountFromFileAndSaveToDatabase(file);
    }
}
