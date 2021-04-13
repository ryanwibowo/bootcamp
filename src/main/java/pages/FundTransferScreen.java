package pages;

import model.Account;
import service.AccountService;
import service.TransactionService;
import utils.AtmUtil;
import validation.Validation;

import java.math.BigDecimal;
import java.util.Scanner;

public class FundTransferScreen {

    private AccountService accountService;
    private TransactionService transactionService;

    public FundTransferScreen(AccountService accountService, TransactionService transactionService) {

        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void process(Account account) {
        FundTransferSummary fundTransferSummary = new FundTransferSummary(accountService, transactionService);
        AtmUtil util = new AtmUtil();
        Validation validate = new Validation();
        LoginScreen loginScreen = new LoginScreen(accountService, transactionService);
        Scanner acc = new Scanner(System.in);
        acc.reset();
        try {
            System.out.println("Please enter destination account and press enter to continue or ");
            System.out.print("press enter to go back to Transaction: ");
            String destination = acc.nextLine();
            validate.validateRegex(destination);
            validate.validateAccount(account, destination);

            System.out.println("Please enter transfer amount and press enter to continue or ");
            System.out.print("press enter to go back to Transaction: ");
            String amount = acc.nextLine();
            validate.validateRegex(amount);
            validate.validateAmount(account.getBalance(), amount);

            long refNumber = util.getReferenceNumber();
            System.out.println("Reference Number: " + refNumber);
            System.out.print("press enter to continue");
            acc.nextLine();

            System.out.println("Transfer Confirmation");
            System.out.println("Destination Account : " + destination);
            System.out.println("Transfer Amount     : $" + amount);
            System.out.println("Reference Number    : " + refNumber);
            System.out.println();
            System.out.println("1. Confirm Trx");
            System.out.println("2. Cancel Trx");
            System.out.print("Choose option[2]: ");
            switch (acc.nextLine()) {
                case "1":
                    fundTransferSummary.process(account, destination, new BigDecimal(amount), refNumber);
                    break;
                default:
                    loginScreen.process();
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            process(account);
        }
    }
}
