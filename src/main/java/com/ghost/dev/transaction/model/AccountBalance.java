package com.ghost.dev.transaction.model;

import java.math.BigDecimal;

public final class AccountBalance {
    public final String account;
    public final BigDecimal amount;
    public final int creditCount;
    public final int debitCount;

    public AccountBalance(String account, BigDecimal amount, int creditCount, int debitCount) {
        this.account = account;
        this.amount = amount;
        this.creditCount = creditCount;
        this.debitCount = debitCount;
    }
}
