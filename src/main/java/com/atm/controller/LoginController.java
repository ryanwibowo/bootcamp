package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.dto.LoginRequest;
import com.atm.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AccountService accountService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, Model attributes) {
        Account account = accountService.validateLogin(request);
        attributes.addAttribute("account", account);
        return "homepage";
    }
}
