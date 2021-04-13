package pages;

import model.Account;
import validation.Validation;

import java.math.BigDecimal;
import java.util.Scanner;

public class WithdrawScreen {

    public void process(Account account) {
        Validation validate = new Validation();
        TransactionScreen transactionScreen = new TransactionScreen();
        SummaryScreen summaryScreen = new SummaryScreen();
        OtherWithdrawScreen otherWithdrawScreen = new OtherWithdrawScreen();
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
                    validate.isBalanceEnough(account.getBalance(), amount10);
                    summaryScreen.process(account, amount10.toString());
                    break;
                case "2":
                    validate.isBalanceEnough(account.getBalance(), amount50);
                    summaryScreen.process(account, amount50.toString());
                    break;
                case "3":
                    validate.isBalanceEnough(account.getBalance(), amount100);
                    summaryScreen.process(account, amount100.toString());
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
