package com.atm.service;

import com.atm.model.Account;
import com.atm.model.dto.LoginRequest;

import java.util.List;

public interface AccountService {
    Account getAccount(String accountNumber);

    List<Account> dumpAccountFromFileAndSaveToDatabase(String file);

    Account validateLogin(LoginRequest request);
}
