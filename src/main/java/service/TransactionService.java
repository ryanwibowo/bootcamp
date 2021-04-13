package service;

import model.Account;
import utils.AtmUtil;

import java.math.BigDecimal;

public class TransactionService {

    private AccountService accountService = AccountService.getAccountService();

    public Account processTransfer(Account account, String amount, String destination) throws Exception {
        AtmUtil util = new AtmUtil();
        BigDecimal currentBalance = util.subtractBalance(account.getBalance(), amount);
        account.setBalance(currentBalance);
        Account destinationAccount = accountService.getAccount(destination);
        destinationAccount.setBalance(util.addBalance(destinationAccount.getBalance(), amount));
        return destinationAccount;
    }
}
