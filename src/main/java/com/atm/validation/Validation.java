package com.atm.validation;

import com.atm.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Validation {

    public void validate(Account account, Account destinationAccount, BigDecimal amount) throws Exception {
        validateRegex(destinationAccount.getAccountNumber(), amount);
        validateAccount(account, destinationAccount.getAccountNumber());
        validateAmount(account.getBalance(), amount);
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

    public void validateAccount(Account account, String destinationNumber) throws Exception {
        if (account.getAccountNumber().equals(destinationNumber)) {
            throw new Exception("Source and destination is same account");
        }
    }
}
