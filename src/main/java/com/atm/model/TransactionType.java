package com.atm.model;

public enum TransactionType {
    WITHDRAW (0),
    FUND_TRANSFER_DEBIT (1),
    FUND_TRANSFER_CREDIT (2);

    public final int id;

    private TransactionType(int id) {
        this.id = id;
    }
}
