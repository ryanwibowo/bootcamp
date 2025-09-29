package com.atm.model.enums;

public enum TransactionType {
    WITHDRAW (1),
    TRANSFER(2);

    public final int id;

    TransactionType(int id) {
        this.id = id;
    }
}
