package service;

import model.Account;
import model.Transaction;
import model.TransactionType;
import utils.AtmUtil;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {

    private List<Transaction> transactions = new ArrayList<>();
    private static TransactionService instance;

    public static TransactionService getTransactionService() {
        if( instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    public Account processTransfer(Account account, BigDecimal amount, LocalTime date, Account destinationAccount) {
        AtmUtil util = new AtmUtil();
        BigDecimal currentBalance = util.subtractBalance(account.getBalance(), amount);
        account.setBalance(currentBalance);
        destinationAccount.setBalance(util.addBalance(destinationAccount.getBalance(), amount));
        setTransactionHistory(TransactionType.FUND_TRANSFER_DEBIT, amount, date, account.getAccountNumber(), destinationAccount.getAccountNumber());
        setTransactionHistory(TransactionType.FUND_TRANSFER_CREDIT, amount, date, destinationAccount.getAccountNumber(), account.getAccountNumber());
        return destinationAccount;
    }

    public void setTransactionHistory(TransactionType transactionType, BigDecimal balance, LocalTime date,
                                             String accountNumber, String destinationAccount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(balance);
        transaction.setTransactionDate(date);
        transaction.setAccountNumber(accountNumber);
        transaction.setDestinationAccount(destinationAccount);
        transactions.add(transaction);
    }

    public List<Transaction> getLast10Transaction(String accountNumber) {
        List<Transaction> transactionList = transactions.stream().filter(transaction ->
                transaction.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
        if (transactionList.size() > 10) {
            return transactionList.subList(transactionList.size() - 10, transactionList.size());
        } else {
            return transactionList;
        }
    }
}
