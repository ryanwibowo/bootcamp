package com.atm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private TransactionType transactionType;
    @Column(precision = 0)
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String accountNumber;
    private String destinationAccount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                transactionType == that.transactionType &&
                Objects.equals(amount.intValue(), that.amount.intValue()) &&
                Objects.equals(transactionDate, that.transactionDate) &&
                Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(destinationAccount, that.destinationAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionType, amount, transactionDate, accountNumber, destinationAccount);
    }
}