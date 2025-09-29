package com.atm.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    private String accountNumber;
    private String destinationNumber;
    private BigDecimal amount;
}
