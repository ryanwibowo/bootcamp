package pages;

import model.Account;
import service.TransactionService;

import java.util.Scanner;

public class FundTransferSummary {

    public void process(Account account, String destination, String amount, long refNumber) throws Exception {
        TransactionScreen transactionScreen = new TransactionScreen();
        TransactionService transactionService = new TransactionService();
        Scanner opt = new Scanner(System.in);
        Account destinationAccount = transactionService.processTransfer(account, amount, destination);
        System.out.println("Fund Transfer Summary");
        System.out.println("Destination Account : " + destination);
        System.out.println("Transfer Amount     : " + amount);
        System.out.println("Reference Number    : " + refNumber);
        System.out.println("Balance             : " + destinationAccount.getBalance());
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
