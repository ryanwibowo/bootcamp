package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.dto.*;
import com.atm.model.enums.TransactionType;
import com.atm.repository.AccountRepository;
import com.atm.repository.TransactionRepository;
import com.atm.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private Validation validation;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account account;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setAccountNumber("123456");
        account.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void processWithdraw_success() {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountNumber("123456");
        request.setAmount(BigDecimal.valueOf(200));

        when(accountService.getAccount("123456")).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WithdrawResponse response = transactionService.processWithdraw(request);

        assertThat(response.getAccountBalance()).isEqualByComparingTo("800");
        assertThat(response.getTransactionType()).isEqualTo(TransactionType.WITHDRAW);
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void processWithdraw_insufficientBalance_throwsException() {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountNumber("123456");
        request.setAmount(BigDecimal.valueOf(2000));

        when(accountService.getAccount("123456")).thenReturn(account);
        doThrow(new RuntimeException("Insufficient balance"))
                .when(validation).validateBalance(account.getBalance(), request.getAmount());

        assertThrows(RuntimeException.class,
                () -> transactionService.processWithdraw(request));

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void processTransfer_success() {
        Account destination = new Account();
        destination.setAccountNumber("654321");
        destination.setBalance(BigDecimal.valueOf(500));

        TransferRequest request = new TransferRequest();
        request.setAccountNumber("123456");
        request.setDestinationNumber("654321");
        request.setAmount(BigDecimal.valueOf(300));

        when(accountService.getAccount("123456")).thenReturn(account);
        when(accountService.getAccount("654321")).thenReturn(destination);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        TransferResponse response = transactionService.processTransfer(request);

        assertThat(response.getAccountBalance()).isEqualByComparingTo("700");
        assertThat(destination.getBalance()).isEqualByComparingTo("800");
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository).saveAll(anyList());
    }

    @Test
    void processTransfer_validationFails_throwsException() {
        Account destination = new Account();
        destination.setAccountNumber("654321");
        destination.setBalance(BigDecimal.valueOf(500));

        TransferRequest request = new TransferRequest();
        request.setAccountNumber("123456");
        request.setDestinationNumber("654321");
        request.setAmount(BigDecimal.valueOf(2000));

        when(accountService.getAccount("123456")).thenReturn(account);
        when(accountService.getAccount("654321")).thenReturn(destination);
        doThrow(new RuntimeException("Insufficient balance"))
                .when(validation).validate(account, destination, request.getAmount());

        assertThrows(RuntimeException.class,
                () -> transactionService.processTransfer(request));

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).saveAll(any());
    }

    @Test
    void getLast10Transaction_returnsTransactions() {
        Transaction t1 = Transaction.builder()
                .accountNumber("123456")
                .transactionDate(LocalDateTime.now())
                .amount(BigDecimal.valueOf(100))
                .destinationNumber(null)
                .accountBalance(BigDecimal.valueOf(900))
                .build();

        when(accountService.getAccount("123456")).thenReturn(account);
        when(transactionRepository.getTop10ByAccountNumberOrDestinationNumberOrderByTransactionDateAsc("123456", "123456"))
                .thenReturn(List.of(t1));

        LastTransactionResponse response = transactionService.getLast10Transaction("123456");

        assertThat(response.getTransactions()).hasSize(1);
        assertThat(response.getTransactions().get(0).getTransactionType()).isEqualTo("WITHDRAW");
    }
}
