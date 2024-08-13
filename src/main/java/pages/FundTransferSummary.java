package pages;

import model.Account;
import service.AccountService;
import service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Scanner;

public class FundTransferSummary {

    private TransactionService transactionService;
    private AccountService accountService;

    public FundTransferSummary(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void process(Account account, String destination, BigDecimal amount, long refNumber) throws Exception {
        TransactionScreen transactionScreen = new TransactionScreen(accountService, transactionService);
        LoginScreen loginScreen = new LoginScreen(accountService, transactionService);
        Scanner opt = new Scanner(System.in);
        LocalTime date = LocalTime.now();
        Account destinationAccount = accountService.getAccount(destination);
        destinationAccount = transactionService.processTransfer(account, amount, date, destinationAccount);
        System.out.println("Fund Transfer Summary");
        System.out.println("Transaction Date    : " + date);
        System.out.println("Destination Account : " + destination);
        System.out.println("Transfer Amount     : " + amount);
        System.out.println("Reference Number    : " + refNumber);
        System.out.println("Balance             : " + destinationAccount.getBalance());
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2]: ");
        switch (opt.nextLine()) {
            case "1":
                transactionScreen.process(account);
                break;
            default:
                loginScreen.process();
                break;
        }
    }
}
