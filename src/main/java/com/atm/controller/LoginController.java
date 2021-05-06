package com.atm.controller;

import com.atm.model.Account;
import com.atm.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login(@RequestParam String accountNumber, @RequestParam String pin, Model attributes)
            throws Exception {
        Account account = accountService.validateLogin(accountNumber, pin);
        attributes.addAttribute(account);
        return "homepage";
    }
}
