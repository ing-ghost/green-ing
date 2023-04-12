package com.ghost.dev.transaction.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class TransactionData {
    public final String creditAccount;
    public final String debitAccount;
    public final BigDecimal amount;

    public TransactionData() {
        creditAccount = null;
        debitAccount = null;
        amount = BigDecimal.ZERO;
    }

    public TransactionData(String creditAccount, String debitAccount, float amount) {
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
        this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
    }

    public TransactionData(String creditAccount, String debitAccount, String amount) {
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
        this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
    }
}
