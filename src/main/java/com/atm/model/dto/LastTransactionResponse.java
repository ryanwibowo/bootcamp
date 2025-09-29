package com.atm.model.dto;

import com.atm.model.Account;
import com.atm.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LastTransactionResponse {
    private Account account;
    private List<Transaction> transactions;
}
