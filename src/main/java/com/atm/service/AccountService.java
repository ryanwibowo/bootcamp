package com.atm.service;

import com.atm.model.Account;

import java.util.List;

public interface AccountService {
    Account getAccount(String accountNumber) throws Exception;
    Account validateLogin(String accountNumber, String pin) throws Exception;
    List<Account> getAccountFromFile(String file);
}
