package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.repository.AccountRepository;
import com.atm.repository.TransactionRepository;
import com.atm.utils.AtmUtil;
import com.atm.validation.Validation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository,
                                  AccountService accountService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    private Transaction processTransfer(Account account, Account destinationAccount, BigDecimal amount) throws Exception {
        Validation validation = new Validation();
        validation.validate(account, destinationAccount, amount);
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

    @Override
    @Transactional
    public Transaction processTransfer(String accountNumber, String destinationNumber, BigDecimal amount) throws Exception {
        Account account = accountService.getAccount(accountNumber);
        Account destinationAccount = accountService.getAccount(destinationNumber);
        return processTransfer(account, destinationAccount, amount);
    }

    private Transaction setTransactionHistory(TransactionType transactionType, BigDecimal balance, LocalDateTime date,
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
    @Transactional
    public Transaction processWithdraw(Account account, BigDecimal balance) throws Exception {
        Validation validation = new Validation();
        validation.isBalanceEnough(account.getBalance(), balance);
        BigDecimal currentBalance = AtmUtil.subtractBalance(account.getBalance(), balance);
        account.setBalance(currentBalance);
        LocalDateTime date = LocalDateTime.now();
        accountRepository.save(account);
        return setTransactionHistory(TransactionType.WITHDRAW, balance, date, account.getAccountNumber(),
                null);
    }

    @Override
    public Page<Transaction> findPaginated(String accountNumber, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        return transactionRepository.findByAccountNumberOrderByTransactionDateDesc(accountNumber, pageable);
    }
}
