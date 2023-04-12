package com.ghost.dev.transaction;

import com.ghost.dev.transaction.model.TransactionData;

import java.util.Random;

public final class TransactionTestData {
    public TransactionData[] staticData() {
        return new TransactionData[]{
                new TransactionData("32309111922661937852684864", "06105023389842834748547303", 10.90f),
                new TransactionData("31074318698137062235845814", "66105036543749403346524547", 200.90f),
                new TransactionData("66105036543749403346524547", "32309111922661937852684864", 50.10f)
        };
    }

    public TransactionData[] testData(int accountCount, int transactionCount) {
        String[] accounts = new String[accountCount];
        TransactionData[] atmData = new TransactionData[transactionCount];
        Random random = new Random();
        for (int i = 0; i < accountCount; i++) {
            accounts[i] = "account:" + i;
        }
        for (int i = 0; i < transactionCount; i++) {
            int credit = random.nextInt(accountCount);
            int debit = random.nextInt(accountCount);
            if (credit == debit) {
                debit = (debit + 1) % accountCount;
            }
            atmData[i] = new TransactionData(
                    accounts[credit],
                    accounts[debit],
                    random.nextInt(0, 10000) + "." + random.nextInt(0, 99)
            );
        }
        return atmData;
    }
}
