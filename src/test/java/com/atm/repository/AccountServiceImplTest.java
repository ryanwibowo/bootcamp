package com.atm.repository;

import com.atm.model.Account;
import com.atm.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456");
        account.setPin("123456");
        account.setName("John Doe");
        account.setBalance(BigDecimal.valueOf(500));
    }

    @Test
    void getAccount_success() {
        when(accountRepository.findByAccountNumber("123456"))
                .thenReturn(Optional.of(account));

        Account result = accountService.getAccount("123456");

        assertThat(result).isEqualTo(account);
        verify(accountRepository).findByAccountNumber("123456");
    }

    @Test
    void getAccount_notFound_throwsException() {
        when(accountRepository.findByAccountNumber("999999"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> accountService.getAccount("999999"));
    }
}
