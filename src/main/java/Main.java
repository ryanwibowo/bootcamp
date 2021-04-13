import pages.LoginScreen;
import service.AccountService;
import service.TransactionService;

public class Main {
    private static AccountService accountService;
    private static TransactionService transactionService;

    public static void main(String args[]) {
        String file = args[0];
        accountService = AccountService.getAccountService(file);
        transactionService = TransactionService.getTransactionService();
        LoginScreen loginScreen = new LoginScreen(accountService, transactionService);
        loginScreen.process();
    }
}
