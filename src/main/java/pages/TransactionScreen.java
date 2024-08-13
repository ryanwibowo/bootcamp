package pages;

import model.Account;
import service.AccountService;
import service.TransactionService;

import java.util.Scanner;

public class TransactionScreen {

    private AccountService accountService;
    private TransactionService transactionService;

    public TransactionScreen(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void process(Account account) {
        WithdrawScreen withdrawScreen = new WithdrawScreen(accountService, transactionService);
        FundTransferScreen fundTransferScreen = new FundTransferScreen(accountService, transactionService);
        LastTransaction lastTransaction = new LastTransaction(accountService, transactionService);
        LoginScreen loginScreen = new LoginScreen(accountService, transactionService);
        Scanner opt = new Scanner(System.in);
        System.out.println("1. Withdraw");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Last 10 Transaction");
        System.out.println("4. Exit");
        System.out.print("Please choose option[3]: ");
        switch (opt.nextLine()) {
            case "1":
                withdrawScreen.process(account);
                break;
            case "2":
                fundTransferScreen.process(account);
                break;
            case "3":
                lastTransaction.process(account);
                break;
            default:
                loginScreen.process();
                break;
        }
    }
}
