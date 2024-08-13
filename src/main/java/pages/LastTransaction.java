package pages;

import model.Account;
import model.Transaction;
import model.TransactionType;
import service.AccountService;
import service.TransactionService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LastTransaction {

    private TransactionService transactionService;
    private List<TransactionType> transactionTypes = Arrays.asList(TransactionType.FUND_TRANSFER_CREDIT,
            TransactionType.FUND_TRANSFER_DEBIT);
    private AccountService accountService;

    public LastTransaction(AccountService accountService, TransactionService transactionService) {

        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void process(Account account) {
        TransactionScreen transactionScreen = new TransactionScreen(accountService, transactionService);
        LoginScreen loginScreen = new LoginScreen(accountService, transactionService);
        List<Transaction> transactionList = transactionService.getLast10Transaction(account.getAccountNumber());
        if (transactionList.isEmpty()) {
            System.out.println("No transaction");
        }
        for (Transaction transaction : transactionList) {
            String print = transaction.getTransactionDate() + " : " + transaction.getTransactionType() +
                    ", $" + transaction.getAmount();
            if (transactionTypes.contains(transaction.getTransactionType())) {
                print = print + ", " + transaction.getDestinationAccount();
            }
            System.out.println(print);
        }
        Scanner opt = new Scanner(System.in);
        System.out.println();
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2]: ");
        switch (opt.nextLine()) {
            case "1":
                transactionScreen.process(account);
                break;
            default:
                loginScreen.process();
        }
    }
}
