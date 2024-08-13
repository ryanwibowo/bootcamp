package pages;

import model.Account;
import model.TransactionType;
import service.AccountService;
import service.TransactionService;
import utils.AtmUtil;
import validation.Validation;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Scanner;

public class WithdrawSummary {

    private TransactionService transactionService;
    private AccountService accountService;

    public WithdrawSummary(AccountService accountService, TransactionService transactionService) {

        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void process(Account account, BigDecimal balance) throws Exception {
        TransactionScreen transactionScreen = new TransactionScreen(accountService, transactionService);
        AtmUtil util = new AtmUtil();
        Validation validate = new Validation();
        LoginScreen loginScreen = new LoginScreen(accountService, transactionService);
        validate.isBalanceEnough(account.getBalance(), balance);
        Scanner opt = new Scanner(System.in);
        BigDecimal currentBalance = util.subtractBalance(account.getBalance(), balance);
        account.setBalance(currentBalance);
        LocalTime date = LocalTime.now();
        transactionService.setTransactionHistory(TransactionType.WITHDRAW, balance, date,
                account.getAccountNumber(), null);
        System.out.println("Summary");
        System.out.println("Date : " + date);
        System.out.println("Withdraw : " + balance);
        System.out.println("Balance : " + currentBalance);
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
