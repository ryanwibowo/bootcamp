package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.dto.*;
import com.atm.model.enums.TransactionType;
import com.atm.repository.AccountRepository;
import com.atm.repository.TransactionRepository;
import com.atm.utils.AtmUtil;
import com.atm.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final AccountService accountService;

    private final Validation validation;

    @Override
    @Transactional
    public WithdrawResponse processWithdraw(WithdrawRequest request) {
        Account account = accountService.getAccount(request.getAccountNumber());
        validation.validateBalance(account.getBalance(), request.getAmount());

        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = setTransaction(TransactionType.WITHDRAW, account.getAccountNumber(),
                null, request.getAmount(), newBalance, newBalance);
        transactionRepository.save(transaction);

        return WithdrawResponse.builder()
                .transactionType(TransactionType.WITHDRAW)
                .transactionDate(transaction.getTransactionDate())
                .accountNumber(account.getAccountNumber())
                .amount(request.getAmount())
                .accountBalance(newBalance)
                .build();
    }

    @Override
    @Transactional
    public TransferResponse processTransfer(TransferRequest request) {
        Account account = accountService.getAccount(request.getAccountNumber());
        Account destination = accountService.getAccount(request.getDestinationNumber());
        validation.validate(account, destination, request.getAmount());

        BigDecimal newBalance = AtmUtil.subtractBalance(account.getBalance(), request.getAmount());
        account.setBalance(newBalance);
        destination.setBalance(AtmUtil.addBalance(destination.getBalance(), request.getAmount()));
        accountRepository.save(account);
        accountRepository.save(destination);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(setTransaction(TransactionType.TRANSFER, account.getAccountNumber(),
                destination.getAccountNumber(), request.getAmount(), newBalance, destination.getBalance()));
        transactionRepository.saveAll(transactionList);

        return TransferResponse.builder()
                .account(account)
                .transactionType(TransactionType.WITHDRAW)
                .transactionDate(transactionList.stream().map(Transaction::getTransactionDate).findFirst()
                        .orElse(LocalDateTime.now()))
                .accountNumber(account.getAccountNumber())
                .destinationNumber(destination.getAccountNumber())
                .amount(request.getAmount())
                .accountBalance(newBalance)
                .build();
    }

    public LastTransactionResponse getLast10Transaction(String accountNumber) {
        Account account = accountService.getAccount(accountNumber);
        List<Transaction> transactions = transactionRepository
                .getTop10ByAccountNumberOrDestinationNumberOrderByTransactionDateAsc(accountNumber, accountNumber);

        for (Transaction transaction : transactions) {
            if (transaction.getDestinationNumber() == null) {
                transaction.setTransactionType(TransactionType.WITHDRAW.toString());
                transaction.setDisplayBalance(transaction.getAccountBalance());
            } else if (transaction.getAccountNumber().equals(account.getAccountNumber())) {
                transaction.setTransactionType("TRANSFER_DEBIT");
                transaction.setDisplayBalance(transaction.getAccountBalance());
            } else {
                transaction.setTransactionType("TRANSFER_CREDIT");
                transaction.setDisplayBalance(transaction.getDisplayBalance());
            }
        }

        LastTransactionResponse response = new LastTransactionResponse();
        response.setAccount(account);
        response.setTransactions(transactions);
        return response;
    }

    private Transaction setTransaction(TransactionType transactionType, String account, String beneficiary,
                                       BigDecimal amount, BigDecimal newBalance, BigDecimal displayBalance) {
        return Transaction.builder()
                .transactionType(transactionType.toString())
                .transactionDate(LocalDateTime.now())
                .accountNumber(account)
                .destinationNumber(beneficiary)
                .amount(amount)
                .accountBalance(newBalance)
                .displayBalance(displayBalance)
                .build();
    }
}
