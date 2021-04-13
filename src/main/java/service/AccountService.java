package service;

import model.Account;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountService {

    private List<Account> accounts;
    private static AccountService instance;

    public static AccountService getAccountService(String file) {
        if( instance == null) {
            instance = new AccountService(file);
        }
        return instance;
    }

    private AccountService(String file) {
        accounts = new ArrayList<>();
        getAccountFromFile(file);
    }

    public Account getAccount(String accountNumber) throws Exception {
        return accounts.stream().filter((account -> account.getAccountNumber().equals(accountNumber)))
                .findFirst().orElseThrow(() -> new Exception("Account not found"));
    }

    private void getAccountFromFile(String file) {
        try (Stream<String> stream = Files.lines(Paths.get(file))) {
            accounts = stream
                    .map(account -> {
                        String[] acc = account.split(",");
                        return buildAccount(acc);
                    })
                    .distinct()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("File not found");
            accounts.add(new Account("1", "John Doe", "012108", BigDecimal.valueOf(30), "112233"));
            accounts.add(new Account("2", "Jane Doe", "932012", BigDecimal.valueOf(30), "112244"));
        }
    }

    private Account buildAccount(String[] acc) {
        Account account = new Account();
        account.setId(acc[0]);
        account.setName(acc[1]);
        account.setPin(acc[2]);
        account.setBalance(new BigDecimal(acc[3]));
        account.setAccountNumber(acc[4]);
        return account;
    }

    public Account validateLogin(String accountNumber, String pin) throws Exception {
        return accounts.stream().filter((account -> account.getAccountNumber().equals(accountNumber)
            && account.getPin().equals(pin))).findFirst().orElseThrow(() -> new Exception("Invalid Account Number/PIN"));
    }
}
