package pages;

import model.Account;
import utils.AtmUtil;
import validation.Validation;

import java.util.Scanner;

public class FundTransferScreen {

    public void process(Account account) {
        FundTransferSummary fundTransferSummary = new FundTransferSummary();
        AtmUtil util = new AtmUtil();
        Validation validate = new Validation();
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
                    fundTransferSummary.process(account, destination, amount, refNumber);
                    break;
                default:
                    acc.close();
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            process(account);
        }
    }
}
