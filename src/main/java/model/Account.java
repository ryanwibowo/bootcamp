package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@ToString
@Getter
@Setter
public class Account {
    private String id;
    private String name;

    @Digits(integer=6, fraction = 0, message = "PIN should have 6 digits length")
    private String pin;
    private BigDecimal balance;

    @Pattern(regexp = "^[0-9]+$", message = "Account Number should only contains numbers")
    @Digits(integer=6, fraction = 0, message = "Account Number should have 6 digits length")
    private String accountNumber;

    public Account() {
        super();
    }
    public Account(String id, String name, String pin, BigDecimal balance, String accountNumber) {
        this.id = id;
        this.name = name;
        this.pin = pin;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }
}