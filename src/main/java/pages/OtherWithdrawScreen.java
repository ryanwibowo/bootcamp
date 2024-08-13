package pages;

import model.Account;
import service.AccountService;
import service.TransactionService;
import validation.Validation;

import java.math.BigDecimal;
import java.util.Scanner;

public class OtherWithdrawScreen {
    private AccountService accountService;
    private TransactionService transactionService;

    public OtherWithdrawScreen(AccountService accountService, TransactionService transactionService) {

        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void process(Account account) {
        WithdrawSummary withdrawSummary = new WithdrawSummary(accountService, transactionService);
        Validation validate = new Validation();
        Scanner opt = new Scanner(System.in);
        opt.reset();
        try {
            System.out.println("Other Withdraw");
            System.out.print("Enter amount to withdraw ");
            String amount = opt.nextLine();
            validate.validateRegex(amount);
            validate.validateBalance(account.getBalance(), amount);
            withdrawSummary.process(account, new BigDecimal(amount));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            process(account);
        }
    }
}
