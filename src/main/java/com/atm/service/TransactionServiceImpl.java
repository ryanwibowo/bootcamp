package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.repository.AccountRepository;
import com.atm.repository.TransactionRepository;
import com.atm.utils.AtmUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private List<Transaction> transactions = new ArrayList<>();

    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository,
                                  AccountService accountService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public Transaction processTransfer(Account account, BigDecimal amount, Account destinationAccount) {
        LocalDateTime date = LocalDateTime.now();
        AtmUtil util = new AtmUtil();
        BigDecimal currentBalance = util.subtractBalance(account.getBalance(), amount);
        account.setBalance(currentBalance);
        destinationAccount.setBalance(util.addBalance(destinationAccount.getBalance(), amount));
        accountRepository.save(account);
        accountRepository.save(destinationAccount);
        setTransactionHistory(TransactionType.FUND_TRANSFER_CREDIT, amount, date, destinationAccount.getAccountNumber(),
                account.getAccountNumber());
        return setTransactionHistory(TransactionType.FUND_TRANSFER_DEBIT, amount, date, account.getAccountNumber(),
                destinationAccount.getAccountNumber());
    }

    public Transaction setTransactionHistory(TransactionType transactionType, BigDecimal balance, LocalDateTime date,
                                      String accountNumber, String destinationAccount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(balance);
        transaction.setTransactionDate(date);
        transaction.setAccountNumber(accountNumber);
        transaction.setDestinationAccount(destinationAccount);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getLast10Transaction(String accountNumber) {
        return transactionRepository.getTop10ByAccountNumberOrderByTransactionDateDesc(accountNumber);
    }

    @Override
    public Transaction processWithdraw(Account account, BigDecimal balance) {
        BigDecimal currentBalance = AtmUtil.subtractBalance(account.getBalance(), balance);
        account.setBalance(currentBalance);
        LocalDateTime date = LocalDateTime.now();
        accountRepository.save(account);
        return setTransactionHistory(TransactionType.WITHDRAW, balance, date, account.getAccountNumber(),
                null);
    }
}
