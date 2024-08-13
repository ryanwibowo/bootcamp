package pages;

import model.Account;
import service.AccountService;
import service.TransactionService;
import validation.Validation;

import java.util.Scanner;

public class LoginScreen {
    private AccountService accountService;
    private TransactionService transactionService;

    public LoginScreen(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void process() {
        TransactionScreen transactionScreen = new TransactionScreen(accountService, transactionService);
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
