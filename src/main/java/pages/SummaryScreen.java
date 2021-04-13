package pages;

import model.Account;
import utils.AtmUtil;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Scanner;

public class SummaryScreen {

    public void process(Account account, String balance) throws Exception {
        TransactionScreen transactionScreen = new TransactionScreen();
        AtmUtil util = new AtmUtil();
        Scanner opt = new Scanner(System.in);
        BigDecimal currentBalance = util.subtractBalance(account.getBalance(), balance);
        account.setBalance(currentBalance);
        System.out.println("Summary");
        System.out.println("Date : " + LocalTime.now().toString());
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
                opt.close();
                break;
        }
    }
}
