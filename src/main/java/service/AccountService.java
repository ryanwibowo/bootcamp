package service;

import com.google.common.collect.Lists;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import model.Account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private List<Account> accounts;
    private static AccountService instance;

    public static AccountService getAccountService() {
        if( instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    private AccountService() {
        Account acc1 = new Account("1", "John Doe", "012108", BigDecimal.valueOf(30), "112233");
        Account acc2 = new Account("2", "Jane Doe", "932012", BigDecimal.valueOf(30), "112244");
        accounts = Lists.newArrayList(acc1, acc2);
    }
    public Account getAccount(String accountNumber) throws Exception {
        return accounts.stream().filter((account -> account.getAccountNumber().equals(accountNumber)))
                .findFirst().orElseThrow(() -> new Exception("Account not found"));
    }

    public List<Account> getAccountFromFile() throws IOException {
        Reader reader = new BufferedReader(new FileReader("d:\\Project\\account.csv"));
        CsvToBean<Account> beans = new CsvToBeanBuilder(reader).withType(Account.class).build();
        return beans.parse();
    }

    public Account validateLogin(String accountNumber, String pin) throws Exception {
        return accounts.stream().filter((account -> account.getAccountNumber().equals(accountNumber)
            && account.getPin().equals(pin))).findFirst().orElseThrow(() -> new Exception("Invalid Account Number/PIN"));
    }
}
