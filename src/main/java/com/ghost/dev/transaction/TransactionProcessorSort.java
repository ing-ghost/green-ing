package com.ghost.dev.transaction;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;
import com.ghost.dev.transaction.model.AccountBalance;
import com.ghost.dev.transaction.model.TransactionData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TransactionProcessorSort implements DataProcessor<TransactionData, List<AccountBalance>> {

    @Override
    public List<AccountBalance> processData(DataInputStream<TransactionData> dataStream) {
        // TreeMap is too slow, it is faster to process data then sort.
        final Map<String, MutableData> accounts = new HashMap<>();

        long start = System.currentTimeMillis();
        List<TransactionData> list = new ArrayList<>();
        for (TransactionData atmData : dataStream) {
            list.add(atmData);
        }

        list.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.creditAccount, o2.creditAccount));
        System.out.println("Sort: " + (System.currentTimeMillis() - start));

        MutableData lastCreditAccountData = null;
        String lastAccount = null;
        for (TransactionData atmData : list) {
            // computeIfAbsent too time expensive, getOrDefault require constant map update also to expensive
            if (lastAccount == null || lastAccount.equals(atmData.creditAccount)) {
                lastCreditAccountData = accounts.get(atmData.creditAccount);
                lastAccount = atmData.creditAccount;
            }
            MutableData creditAccount = lastCreditAccountData;
            MutableData debitAccount = accounts.get(atmData.debitAccount);

            if (creditAccount == null) {
                creditAccount = new MutableData();
                accounts.put(atmData.creditAccount, creditAccount);
                lastCreditAccountData = creditAccount;
            }

            if (debitAccount == null) {
                debitAccount = new MutableData();
                accounts.put(atmData.debitAccount, debitAccount);
            }

            creditAccount.balance = creditAccount.balance.subtract(atmData.amount);
            debitAccount.balance = debitAccount.balance.add(atmData.amount);

            creditAccount.credit++;
            debitAccount.debit++;
        }

        return accounts
                .entrySet()
                .stream()
                .map(entry -> new AccountBalance(
                                entry.getKey(),
                                entry.getValue().balance,
                                entry.getValue().credit,
                                entry.getValue().debit
                        )
                ).sorted((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.account, o2.account))
                .toList();
    }

    private static class MutableData {
        BigDecimal balance = BigDecimal.ZERO;
        int debit = 0;
        int credit = 0;
    }

}
