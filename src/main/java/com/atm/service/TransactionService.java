package com.atm.service;

import com.atm.model.Transaction;
import com.atm.model.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    LastTransactionResponse getLast10Transaction(String accountNumber);

    WithdrawResponse processWithdraw(WithdrawRequest request);

    TransferResponse processTransfer(TransferRequest transferRequest);
}
