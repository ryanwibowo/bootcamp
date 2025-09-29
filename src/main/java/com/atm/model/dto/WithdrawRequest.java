package com.atm.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {
    private String accountNumber;
    private BigDecimal amount;
}
