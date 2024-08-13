package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@ToString
@Getter
@Setter
public class Transaction {
    private TransactionType transactionType;
    private BigDecimal amount;
    private LocalTime transactionDate;
    private String accountNumber;
    private String destinationAccount;
}