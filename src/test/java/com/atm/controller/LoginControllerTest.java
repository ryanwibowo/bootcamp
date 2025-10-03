package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.dto.LoginRequest;
import com.atm.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private Model model;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnHomepageAndAddAccountToModel_whenValidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setAccountNumber("123456");
        request.setPin("123456");

        Account mockAccount = new Account();
        mockAccount.setAccountNumber("123456");
        mockAccount.setPin("123456");
        mockAccount.setName("John Doe");

        when(accountService.validateLogin(request)).thenReturn(mockAccount);

        String viewName = loginController.login(request, model);

        assertThat(viewName).isEqualTo("homepage");
        verify(accountService).validateLogin(request);
        verify(model).addAttribute("account", mockAccount);
    }

    @Test
    void login_shouldThrowException_whenInvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setAccountNumber("111111");
        request.setPin("999999");

        when(accountService.validateLogin(request))
                .thenThrow(new RuntimeException("Invalid account or PIN"));

        assertThatThrownBy(() -> loginController.login(request, model))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid account or PIN");

        verify(accountService).validateLogin(request);
        verify(model, never()).addAttribute(eq("account"), any());
    }
}
