package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.dto.*;
import com.atm.service.AccountService;
import com.atm.service.TransactionService;
import com.atm.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Mock
    private Validation validation;

    @Mock
    private Model model;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void withdraw_shouldReturnWithdrawPageAndAddRequest() {
        String accountNumber = "123456";

        String viewName = transactionController.withdraw(accountNumber, model);

        assertThat(viewName).isEqualTo("withdraw");
        verify(model).addAttribute(eq("transactionRequest"), any(WithdrawRequest.class));
    }

    @Test
    void processWithdraw_shouldReturnSummaryAndAddTransaction() {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountNumber("123456");

        WithdrawResponse mockResponse = new WithdrawResponse();
        when(transactionService.processWithdraw(request)).thenReturn(mockResponse);

        String viewName = transactionController.process(request, model);

        assertThat(viewName).isEqualTo("withdrawSummary");
        verify(transactionService).processWithdraw(request);
        verify(model).addAttribute("transaction", mockResponse);
    }

    @Test
    void processWithdraw_shouldThrowException_whenServiceFails() {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountNumber("111111");

        when(transactionService.processWithdraw(request))
                .thenThrow(new RuntimeException("Insufficient balance"));

        assertThatThrownBy(() -> transactionController.process(request, model))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Insufficient balance");

        verify(transactionService).processWithdraw(request);
        verify(model, never()).addAttribute(eq("transaction"), any());
    }

    @Test
    void lastTransaction_shouldReturnPageAndAddResponse() {
        String accountNumber = "123456";
        LastTransactionResponse response = new LastTransactionResponse();

        when(transactionService.getLast10Transaction(accountNumber)).thenReturn(response);

        String viewName = transactionController.lastTransaction(accountNumber, model);

        assertThat(viewName).isEqualTo("lastTransaction");
        verify(transactionService).getLast10Transaction(accountNumber);
        verify(model).addAttribute("lastTransaction", response);
    }

    @Test
    void lastTransaction_shouldThrowException_whenServiceFails() {
        String accountNumber = "111111";

        when(transactionService.getLast10Transaction(accountNumber))
                .thenThrow(new RuntimeException("Account not found"));

        assertThatThrownBy(() -> transactionController.lastTransaction(accountNumber, model))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account not found");

        verify(transactionService).getLast10Transaction(accountNumber);
        verify(model, never()).addAttribute(eq("lastTransaction"), any());
    }

    @Test
    void transfer_shouldReturnTransferPageAndAddAccount() {
        String accountNumber = "123456";
        Account mockAccount = new Account();
        mockAccount.setAccountNumber("123456");

        when(accountService.getAccount(accountNumber)).thenReturn(mockAccount);

        String viewName = transactionController.transfer(accountNumber, model);

        assertThat(viewName).isEqualTo("transfer");
        verify(accountService).getAccount(accountNumber);
        verify(model).addAttribute(mockAccount);
    }

    @Test
    void transfer_shouldThrowException_whenAccountNotFound() {
        String accountNumber = "999999";

        when(accountService.getAccount(accountNumber))
                .thenThrow(new RuntimeException("Account not found"));

        assertThatThrownBy(() -> transactionController.transfer(accountNumber, model))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account not found");

        verify(accountService).getAccount(accountNumber);
        verify(model, never()).addAttribute(any());
    }

    @Test
    void processTransfer_shouldReturnSummaryAndAddResponse() {
        TransferRequest request = new TransferRequest();
        TransferResponse response = new TransferResponse();

        when(transactionService.processTransfer(request)).thenReturn(response);

        String viewName = transactionController.processTransfer(request, model);

        assertThat(viewName).isEqualTo("transferSummary");
        verify(transactionService).processTransfer(request);
        verify(model).addAttribute("transfer", response);
    }

    @Test
    void processTransfer_shouldThrowException_whenServiceFails() {
        TransferRequest request = new TransferRequest();

        when(transactionService.processTransfer(request))
                .thenThrow(new RuntimeException("Transfer failed"));

        assertThatThrownBy(() -> transactionController.processTransfer(request, model))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Transfer failed");

        verify(transactionService).processTransfer(request);
        verify(model, never()).addAttribute(eq("transfer"), any());
    }
}
