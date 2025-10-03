package com.atm.service;

import com.atm.exception.ValidationException;
import com.atm.model.Account;
import com.atm.model.dto.LoginRequest;
import com.atm.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccount_shouldReturnAccount_whenExists() {
        Account account = new Account();
        account.setAccountNumber("123456");
        when(accountRepository.findByAccountNumber("123456"))
                .thenReturn(Optional.of(account));

        Account result = accountService.getAccount("123456");

        assertThat(result).isEqualTo(account);
    }

    @Test
    void getAccount_shouldThrowException_whenNotFound() {
        when(accountRepository.findByAccountNumber("999999"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount("999999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account not found");
    }

    @Test
    void validateLogin_shouldReturnAccount_whenPinIsValid_firstLogin() {
        LoginRequest request = new LoginRequest();
        request.setAccountNumber("123456");
        request.setPin("123456");
        request.setFirstLogin(true);

        Account account = new Account();
        account.setAccountNumber("123456");
        account.setPin("encoded-pin");

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("123456", "encoded-pin")).thenReturn(true);

        Account result = accountService.validateLogin(request);

        assertThat(result).isEqualTo(account);
    }

    @Test
    void validateLogin_shouldThrowValidationException_whenInvalidPin_firstLogin() {
        LoginRequest request = new LoginRequest();
        request.setAccountNumber("123456");
        request.setPin("wrong-pin");
        request.setFirstLogin(true);

        Account account = new Account();
        account.setAccountNumber("123456");
        account.setPin("encoded-pin");

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("wrong-pin", "encoded-pin")).thenReturn(false);

        assertThatThrownBy(() -> accountService.validateLogin(request))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid PIN");
    }

    @Test
    void validateLogin_shouldReturnAccount_whenPinMatches_nonFirstLogin() {
        LoginRequest request = new LoginRequest();
        request.setAccountNumber("123456");
        request.setPin("123456");
        request.setFirstLogin(false);

        Account account = new Account();
        account.setAccountNumber("123456");
        account.setPin("123456");

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));

        Account result = accountService.validateLogin(request);

        assertThat(result).isEqualTo(account);
    }

    @Test
    void validateLogin_shouldThrowValidationException_whenPinDoesNotMatch_nonFirstLogin() {
        LoginRequest request = new LoginRequest();
        request.setAccountNumber("123456");
        request.setPin("000000");
        request.setFirstLogin(false);

        Account account = new Account();
        account.setAccountNumber("123456");
        account.setPin("123456");

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.validateLogin(request))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid PIN");
    }

    @Test
    void dumpAccountFromFileAndSaveToDatabase_shouldParseFileAndSaveAccounts() {
        String file = "test.csv";

        List<String> lines = List.of("1,John Doe,123456,100,111111");
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.lines(Paths.get(file)))
                    .thenReturn(lines.stream());

            when(passwordEncoder.encode("123456")).thenReturn("encoded-pin");

            List<Account> result = accountService.dumpAccountFromFileAndSaveToDatabase(file);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("John Doe");
            assertThat(result.get(0).getAccountNumber()).isEqualTo("111111");
            assertThat(result.get(0).getPin()).isEqualTo("encoded-pin");

            verify(accountRepository).saveAll(result);
        }
    }

    @Test
    void dumpAccountFromFileAndSaveToDatabase_shouldUseFallbackAccounts_whenIOException() {
        String file = "notfound.csv";

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.lines(Paths.get(file)))
                    .thenThrow(new IOException("File not found"));

            List<Account> result = accountService.dumpAccountFromFileAndSaveToDatabase(file);

            assertThat(result).hasSize(2); // fallback accounts
            assertThat(result.get(0).getName()).isEqualTo("John Doe");
            assertThat(result.get(1).getName()).isEqualTo("Jane Doe");

            verify(accountRepository).saveAll(result);
        }
    }
}
