package com.atm.service;

import com.atm.exception.ValidationException;
import com.atm.model.Account;
import com.atm.model.dto.LoginRequest;
import com.atm.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private List<Account> accounts;

    @Override
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public Account validateLogin(LoginRequest request) {
        Account account = getAccount(request.getAccountNumber());
        if (!isValidPin(request, account)) {
            throw new ValidationException("Invalid PIN");
        }
        return account;
    }

    @Override
    @Transactional
    public List<Account> dumpAccountFromFileAndSaveToDatabase(String file) {
        try (Stream<String> stream = Files.lines(Paths.get(file))) {
            accounts = stream
                    .map(account -> {
                        String[] acc = account.split(",");
                        return buildAccount(acc);
                    })
                    .filter(distinctByKey(Account::getAccountNumber))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("File not found");
            accounts.add(new Account(1L, "John Doe", "012108", BigDecimal.valueOf(30), "112233"));
            accounts.add(new Account(2L, "Jane Doe", "932012", BigDecimal.valueOf(30), "112244"));
        }
        accountRepository.saveAll(accounts);
        return accounts;
    }

    private Account buildAccount(String[] acc) {
        Account account = new Account();
        account.setName(acc[1]);
        account.setPin(passwordEncoder.encode(acc[2]));
        account.setBalance(new BigDecimal(acc[3]));
        account.setAccountNumber(acc[4]);
        return account;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private boolean isValidPin(LoginRequest request, Account account) {
        return request.isFirstLogin()
                ? passwordEncoder.matches(request.getPin(), account.getPin())
                : request.getPin().equals(account.getPin());
    }
}
