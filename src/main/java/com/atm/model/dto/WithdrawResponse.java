package com.atm.model.dto;

import com.atm.model.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawResponse {
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private String accountNumber;
    private BigDecimal amount;
    private BigDecimal accountBalance;
}

