package com.atm.repository;

import com.atm.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository  extends JpaRepository<Transaction, Long>  {
    List<Transaction> getTop10ByAccountNumberOrderByTransactionDateDesc(String accountNumber);
    Page<Transaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber, Pageable pageable);
}
