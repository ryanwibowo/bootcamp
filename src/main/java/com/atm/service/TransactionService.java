package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Transaction processWithdraw(Account account, BigDecimal balance) throws Exception;
    List<Transaction> getLast10Transaction(String accountNumber);
    Transaction processTransfer(String accountNumber, String destinationNumber, BigDecimal amount) throws Exception;
    Page<Transaction> findPaginated(String accountNumber, int currentPage, int pageSize);
}
