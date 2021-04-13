package com.atm.controller;

import com.atm.model.Account;
import com.atm.validation.Validation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private Validation validation;

    public LoginController(Validation validation) {
        this.validation = validation;
    }

    @GetMapping("/login")
    public String login(@RequestParam String accountNumber, @RequestParam String pin, Model attributes)
            throws Exception {
        Account account = validation.validateLogin(accountNumber, pin);
        attributes.addAttribute(account);
        return "homepage";
    }
}
