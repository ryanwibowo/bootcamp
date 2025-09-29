package com.atm.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String accountNumber;
    private String pin;
    private boolean firstLogin = true;
}
