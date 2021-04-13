package pages;

import model.Account;
import validation.Validation;

import java.util.Scanner;

public class LoginScreen {

    public void process() {
        TransactionScreen transactionScreen = new TransactionScreen();
        Validation validate = new Validation();
        Scanner acc = new Scanner(System.in);
        acc.reset();
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = acc.nextLine();
            validate.validate(accountNumber);
            System.out.print("Enter PIN: ");
            String pinAccount = acc.nextLine();
            validate.validate(pinAccount);
            Account account = validate.validateLogin(accountNumber, pinAccount);
            transactionScreen.process(account);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            process();
        }

    }
}
