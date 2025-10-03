package com.atm.service;

import com.atm.model.dto.*;

public interface TransactionService {
    LastTransactionResponse getLast10Transaction(String accountNumber);

    WithdrawResponse processWithdraw(WithdrawRequest request);

    TransferResponse processTransfer(TransferRequest transferRequest);
}
