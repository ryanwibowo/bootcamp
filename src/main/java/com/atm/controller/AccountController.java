package com.atm.controller;

import com.atm.model.Account;
import com.atm.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/dumpAccountFromFileAndSaveToDatabase")
    public ResponseEntity<List<Account>> dumpAccountFromFileAndSaveToDatabase(
            @Validated @RequestParam("file") String file) {
        List<Account> accounts = accountService.dumpAccountFromFileAndSaveToDatabase(file);
        return ResponseEntity.ok(accounts);
    }
}
