package pages;

import model.Account;

import java.util.Scanner;

public class TransactionScreen {

    public void process(Account account) {
        WithdrawScreen withdrawScreen = new WithdrawScreen();
        FundTransferScreen fundTransferScreen = new FundTransferScreen();
        Scanner opt = new Scanner(System.in);
        System.out.println("1. Withdraw");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Exit");
        System.out.print("Please choose option[3]: ");
        switch (opt.nextLine()) {
            case "1":
                withdrawScreen.process(account);
                break;
            case "2":
                fundTransferScreen.process(account);
                break;
            default:
                opt.close();
        }
    }
}
