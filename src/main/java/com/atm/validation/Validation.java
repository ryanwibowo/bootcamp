package com.atm.validation;

import com.atm.model.Account;
import com.atm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Validation {

    @Autowired
    private AccountService accountService;

    public Validation(AccountService accountService) {
        this.accountService = accountService;
    }

    public void validate(Account account, String destinationNumber, BigDecimal amount) throws Exception {
        validateRegex(destinationNumber, amount);
        validateAccount(account, destinationNumber);
        validateAmount(account.getBalance(), amount);
    }

    public Account validateLogin(String accountNumber, String pin) throws Exception {
        return accountService.validateLogin(accountNumber, pin);
    }

    public void validateRegex(String destinationNumber, BigDecimal amount) throws Exception {
        if (destinationNumber.isEmpty() || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new Exception("Value can't be empty or 0");
        }
        if (!destinationNumber.matches("^[0-9]+$")) {
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

    public void validateAmount(BigDecimal currentBalance, BigDecimal amount) throws Exception {
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
