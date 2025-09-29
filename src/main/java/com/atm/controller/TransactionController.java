package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.dto.*;
import com.atm.service.AccountService;
import com.atm.service.TransactionService;
import com.atm.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private final AccountService accountService;

    private final Validation validation;

    @GetMapping("/withdraw")
    public String withdraw(@RequestParam("accountNumber") String accountNumber, Model model) {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountNumber(accountNumber);
        model.addAttribute("transactionRequest", request);
        return "withdraw";
    }

    @PostMapping("/processWithdraw")
    public String process(@ModelAttribute("transactionRequest") WithdrawRequest request, Model model) {
        WithdrawResponse response = transactionService.processWithdraw(request);
        model.addAttribute("transaction", response);
        return "withdrawSummary";
    }


    @GetMapping("/otherWithdraw")
    public String otherWithdraw(@RequestParam("accountNumber") String accountNumber, Model model) {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountNumber(accountNumber);
        model.addAttribute("transactionRequest", request);
        return "otherWithdraw";
    }

    @GetMapping(value = "/lastTransaction")
    public String lastTransaction(@RequestParam("accountNumber") String accountNumber, Model model) {
        LastTransactionResponse response = transactionService.getLast10Transaction(accountNumber);
        model.addAttribute("lastTransaction", response);
        return "lastTransaction";
    }

    @GetMapping("/transfer")
    public String transfer(@RequestParam("accountNumber") String accountNumber, Model attributes) {
        Account account = accountService.getAccount(accountNumber);
        attributes.addAttribute(account);
        return "transfer";
    }

    @PostMapping("/processTransfer")
    public String processTransfer(@ModelAttribute("transferRequest") TransferRequest transferRequest,
                                  Model attributes) {
        TransferResponse transferResponse = transactionService.processTransfer(transferRequest);
        attributes.addAttribute("transfer", transferResponse);
        return "transferSummary";
    }
}
