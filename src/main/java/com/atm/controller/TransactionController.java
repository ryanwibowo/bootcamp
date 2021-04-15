package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionDto;
import com.atm.service.AccountService;
import com.atm.service.TransactionService;
import com.atm.validation.Validation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class TransactionController {

    private TransactionService transactionService;

    private AccountService accountService;

    private Validation validation;

    public TransactionController(TransactionService transactionService, AccountService accountService,
                                 Validation validation) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.validation = validation;
    }

    @GetMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, Model attributes) throws Exception {
        Account account = accountService.getAccount(accountNumber);
        attributes.addAttribute(account);
        return "withdraw";
    }

    @PostMapping("/processWithdraw")
    public String process(@RequestParam String accountNumber, @RequestParam BigDecimal balance,
                          Model attributes) throws Exception {
        Account account = accountService.getAccount(accountNumber);
        validation.isBalanceEnough(account.getBalance(), balance);
        Transaction transaction = transactionService.processWithdraw(account, balance);
        attributes.addAttribute(account);
        attributes.addAttribute(transaction);
        return "withdrawSummary";
    }

    @GetMapping("/otherWithdraw")
    public String otherWithdraw(@RequestParam String accountNumber, Model attributes) throws Exception {
        Account account = accountService.getAccount(accountNumber);
        attributes.addAttribute(account);
        return "otherWithdraw";
    }

    @GetMapping(value = "/lastTransaction")
    public String lastTransaction(@RequestParam String accountNumber, Model attributes) throws Exception {
        Account account = accountService.getAccount(accountNumber);
        List<Transaction> transactions = transactionService.getLast10Transaction(accountNumber);
        attributes.addAttribute("transactionList", new TransactionDto(account, transactions));
        return "lastTransaction";
    }

    @GetMapping("/transfer")
    public String transfer(@RequestParam String accountNumber, Model attributes) throws Exception {
        Account account = accountService.getAccount(accountNumber);
        attributes.addAttribute(account);
        return "transfer";
    }

    @GetMapping("/processTransfer")
    public String processTransfer(@RequestParam String accountNumber, @RequestParam String destinationNumber,
                                  @RequestParam BigDecimal amount, Model attributes) throws Exception {
        Account account = accountService.getAccount(accountNumber);
        Account destinationAccount = accountService.getAccount(destinationNumber);
        validation.validate(account, destinationAccount, amount);
        Transaction transaction = transactionService.processTransfer(account, destinationAccount, amount);
        attributes.addAttribute(account);
        attributes.addAttribute(transaction);
        return "transferSummary";
    }
}
