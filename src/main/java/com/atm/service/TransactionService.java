package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Transaction processWithdraw(Account account, BigDecimal balance);
    List<Transaction> getLast10Transaction(String accountNumber);
    Transaction processTransfer(Account account, BigDecimal amount, Account destinationAccount);
}
