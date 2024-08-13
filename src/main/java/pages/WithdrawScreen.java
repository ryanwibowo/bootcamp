package pages;

import model.Account;
import service.AccountService;
import service.TransactionService;

import java.math.BigDecimal;
import java.util.Scanner;

public class WithdrawScreen {

    private AccountService accountService;
    private TransactionService transactionService;

    public WithdrawScreen(AccountService accountService, TransactionService transactionService) {

        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void process(Account account) {
        TransactionScreen transactionScreen = new TransactionScreen(accountService, transactionService);
        WithdrawSummary withdrawSummary = new WithdrawSummary(accountService, transactionService);
        OtherWithdrawScreen otherWithdrawScreen = new OtherWithdrawScreen(accountService, transactionService);
        Scanner opt = new Scanner(System.in);
        opt.reset();
        try {
            BigDecimal amount10 = new BigDecimal(10);
            BigDecimal amount50 = new BigDecimal(50);
            BigDecimal amount100 = new BigDecimal(100);
            System.out.println("1. $" + amount10);
            System.out.println("2. $" + amount50);
            System.out.println("3. $" + amount100);
            System.out.println("4. Others");
            System.out.println("5. Back");
            System.out.print("Please choose option[5]: ");
            switch (opt.nextLine()) {
                case "1":
                    withdrawSummary.process(account, amount10);
                    break;
                case "2":
                    withdrawSummary.process(account, amount50);
                    break;
                case "3":
                    withdrawSummary.process(account, amount100);
                    break;
                case "4":
                    otherWithdrawScreen.process(account);
                    break;
                default:
                    transactionScreen.process(account);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            process(account);
        }
    }
}
