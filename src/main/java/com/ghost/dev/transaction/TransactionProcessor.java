package com.ghost.dev.transaction;

import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.ghost.dev.transaction.model.AccountBalance;
import com.ghost.dev.transaction.model.TransactionData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TransactionProcessor implements DataProcessor<EmptyDataProcessorConfig, TransactionData, List<AccountBalance>> {

    @Override
    public List<AccountBalance> processData(EmptyDataProcessorConfig config, DataInputStream<TransactionData> dataStream) {
        // TreeMap is too slow, it is faster to process data then sort.
        final Map<String, MutableData> accounts = new HashMap<>();

        for (TransactionData atmData : dataStream) {
            // computeIfAbsent too time expensive, getOrDefault require constant map update also to expensive
            MutableData creditAccount = accounts.get(atmData.creditAccount);
            MutableData debitAccount = accounts.get(atmData.debitAccount);

            if (creditAccount == null) {
                creditAccount = new MutableData();
                accounts.put(atmData.creditAccount, creditAccount);
            }

            if (debitAccount == null) {
                debitAccount = new MutableData();
                accounts.put(atmData.debitAccount, debitAccount);
            }

            creditAccount.balance = creditAccount.balance.add(atmData.amount);
            debitAccount.balance = debitAccount.balance.subtract(atmData.amount);

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
