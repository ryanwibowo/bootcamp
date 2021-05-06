package com.atm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransactionDto {
    private Account account;
    private List<Transaction> transactions;

    public TransactionDto(Account account, List<Transaction> transactions) {
        this.account = account;
        this.transactions = transactions;
    }
}
