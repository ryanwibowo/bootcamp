package com.atm.service;

import com.atm.model.Account;
import com.atm.model.AccountDetails;
import com.atm.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private List<Account> accounts;

    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(String accountNumber) throws Exception {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new Exception("Account not found"));
    }

    @Override
    public Account validateLogin(String accountNumber, String pin) throws Exception {
        return accountRepository.findByAccountNumberAndPin(accountNumber, pin)
                .orElseThrow(() -> new Exception("Invalid Account Number/PIN"));
    }

    @Override
    public List<Account> getAccountFromFile(String file) {
        try (Stream<String> stream = Files.lines(Paths.get(file))) {
            accounts = stream
                    .map(account -> {
                        String[] acc = account.split(",");
                        return buildAccount(acc);
                    })
                    .filter(distinctByKey(Account::getAccountNumber))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("File not found");
            accounts.add(new Account(1l, "John Doe", "012108", BigDecimal.valueOf(30), "112233"));
            accounts.add(new Account(2l, "Jane Doe", "932012", BigDecimal.valueOf(30), "112244"));
        }
        return accounts;
    }

    private Account buildAccount(String[] acc) {
        Account account = new Account();
        account.setId(new Long(acc[0]));
        account.setName(acc[1]);
        account.setPin(passwordEncoder.encode(acc[2]));
        account.setBalance(new BigDecimal(acc[3]));
        account.setAccountNumber(acc[4]);
        return account;
    }

    //distinct by key
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = new Account();
        try {
            account = getAccount(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AccountDetails(account);
    }
}
