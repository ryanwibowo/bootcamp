package utils;

import java.math.BigDecimal;

public class AtmUtil {

    public BigDecimal subtractBalance(BigDecimal currentBalance, String amount) {
        return currentBalance.subtract(new BigDecimal(amount));
    }

    public BigDecimal addBalance(BigDecimal currentBalance, String amount) {
        return currentBalance.add(new BigDecimal(amount));
    }

    public long getReferenceNumber() {
        return (long) (Math.random() * (999999 - 100000 + 1) + 100000);
    }
}
