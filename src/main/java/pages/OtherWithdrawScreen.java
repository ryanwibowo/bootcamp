package pages;

import model.Account;
import validation.Validation;

import java.util.Scanner;

public class OtherWithdrawScreen {
    public void process(Account account) {
        SummaryScreen summaryScreen = new SummaryScreen();
        Validation validate = new Validation();
        Scanner opt = new Scanner(System.in);
        opt.reset();
        try {
            System.out.println("Other Withdraw");
            System.out.print("Enter amount to withdraw ");
            String amount = opt.nextLine();
            validate.validateRegex(amount);
            validate.validateBalance(account.getBalance(), amount);
            summaryScreen.process(account, amount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            process(account);
        }
    }
}
