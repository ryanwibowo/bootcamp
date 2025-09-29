package com.atm.model;

import com.atm.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionType;

    @Column(precision = 0)
    private BigDecimal amount;

    private LocalDateTime transactionDate;

    private String accountNumber;

    private String destinationNumber;

    private BigDecimal accountBalance;

    private BigDecimal displayBalance;
}