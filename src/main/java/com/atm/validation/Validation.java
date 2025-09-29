package com.atm.validation;

import com.atm.exception.ValidationException;
import com.atm.model.Account;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class Validation {

    private static final BigDecimal MAX_WITHDRAW_AMOUNT = BigDecimal.valueOf(1000);

    public void validate(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        validateDestination(destinationAccount.getAccountNumber());
        validateAmount(amount);
        validateNotSameAccount(sourceAccount, destinationAccount.getAccountNumber());
        validateBalance(sourceAccount.getBalance(), amount);
    }

    public void validateBalance(BigDecimal currentBalance, BigDecimal amount) {
        if (currentBalance.compareTo(amount) < 0) {
            throw new ValidationException("Insufficient balance. Current: $" + currentBalance + ", Required: $" + amount);
        }
    }

    private void validateDestination(String destinationNumber) {
        if (destinationNumber == null || destinationNumber.isEmpty()) {
            throw new ValidationException("Destination account number cannot be empty");
        }
        if (!destinationNumber.matches("^\\d+$")) {
            throw new ValidationException("Destination account number must contain only digits");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than 0");
        }
        if (amount.compareTo(MAX_WITHDRAW_AMOUNT) > 0) {
            throw new ValidationException("Maximum withdrawal amount is $1000. Requested: $" + amount);
        }
    }

    private void validateNotSameAccount(Account account, String destinationNumber) {
        if (account.getAccountNumber().equals(destinationNumber)) {
            throw new ValidationException("Source and destination accounts cannot be the same");
        }
    }
}
