package com.atm.utils;

import java.math.BigDecimal;

public class AtmUtil {

    public static BigDecimal subtractBalance(BigDecimal currentBalance, BigDecimal amount) {
        return currentBalance.subtract(amount);
    }

    public BigDecimal addBalance(BigDecimal currentBalance, BigDecimal amount) {
        return currentBalance.add(amount);
    }

    public long getReferenceNumber() {
        return (long) (Math.random() * (999999 - 100000 + 1) + 100000);
    }
}
