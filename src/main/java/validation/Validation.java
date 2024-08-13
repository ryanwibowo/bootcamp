package validation;

import model.Account;
import service.AccountService;

import java.math.BigDecimal;

public class Validation {

    private AccountService accountService = AccountService.getAccountService(null);

    public void validate(String value) throws Exception {
        if (value.length() != 6) {
            throw new Exception("Value should have 6 digits length");
        }
        validateRegex(value);
    }

    public Account validateLogin(String accountNumber, String pin) throws Exception {
        return accountService.validateLogin(accountNumber, pin);
    }

    public void validateRegex(String value) throws Exception {
        if (value.isEmpty()) {
            throw new Exception("Value can't be empty");
        }
        if (!value.matches("^[0-9]+$")) {
            throw new Exception("Value should only contains numbers");
        }
    }

    public void isBalanceEnough(BigDecimal currentBalance, BigDecimal amount) throws Exception {
        if (currentBalance.compareTo(amount) < 0) {
            throw new Exception("Insufficient balance $" + currentBalance);
        }
    }

    public void validateBalance(BigDecimal currentBalance, String value) throws Exception {
        BigDecimal amount = new BigDecimal(value);
        if (Integer.valueOf(value) % 10 > 0) {
            throw new Exception("Invalid amount");
        }
        if (amount.compareTo(BigDecimal.valueOf(1000l)) > 0) {
            throw new Exception("Maximum amount to withdraw is $1000");
        }
        isBalanceEnough(currentBalance, amount);
    }

    public void validateAmount(BigDecimal currentBalance, String value) throws Exception {
        BigDecimal amount = new BigDecimal(value);
        if (amount.compareTo(BigDecimal.valueOf(1000l)) > 0) {
            throw new Exception("Maximum amount to withdraw is $1000");
        }
        isBalanceEnough(currentBalance, amount);
    }

    public void validateAccount(Account account, String value) throws Exception {
        if (account.getAccountNumber().equals(value)) {
            throw new Exception("Source and destination is same account");
        }
        accountService.getAccount(account.getAccountNumber());
    }
}
