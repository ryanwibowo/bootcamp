package com.atm.utils;

import java.math.BigDecimal;

public class AtmUtil {
    public static BigDecimal subtractBalance(BigDecimal currentBalance, BigDecimal amount) {
        return currentBalance.subtract(amount);
    }

    public static BigDecimal addBalance(BigDecimal currentBalance, BigDecimal amount) {
        return currentBalance.add(amount);
    }
}
