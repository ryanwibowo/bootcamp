package com.atm.controller;

import com.atm.model.Account;
import com.atm.repository.AccountRepository;
import com.atm.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AccountController {

    private AccountService accountService;

    private AccountRepository accountRepository;

    public AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/getAccountFromFile")
    public ResponseEntity<List<Account>> getAccountFromFile(@Validated @RequestParam String file) {
        List<Account> accounts = accountService.getAccountFromFile(file);
        return ResponseEntity.ok(accounts);
    }
}
