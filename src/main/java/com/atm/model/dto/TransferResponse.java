package com.atm.model.dto;

import com.atm.model.Account;
import com.atm.model.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransferResponse {
    private Account account;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private String accountNumber;
    private String destinationNumber;
    private BigDecimal amount;
    private BigDecimal accountBalance;
}
