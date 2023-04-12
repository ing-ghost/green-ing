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

public final class TransactionProcessorImmutable implements DataProcessor<EmptyDataProcessorConfig, TransactionData, List<AccountBalance>> {

    @Override
    public List<AccountBalance> processData(EmptyDataProcessorConfig config, DataInputStream<TransactionData> dataStream) {
        final Map<String, BigDecimal> accounts = new HashMap<>();
        final Map<String, Integer> debitCount = new HashMap<>();
        final Map<String, Integer> creditCount = new HashMap<>();

        for (TransactionData atmData : dataStream) {
            BigDecimal creditBalance = accounts.getOrDefault(atmData.creditAccount, BigDecimal.ZERO);
            BigDecimal debitBalance = accounts.getOrDefault(atmData.debitAccount, BigDecimal.ZERO);

            creditBalance = creditBalance.subtract(atmData.amount);
            debitBalance = debitBalance.add(atmData.amount);

            accounts.put(atmData.creditAccount, creditBalance);
            accounts.put(atmData.debitAccount, debitBalance);

            creditCount.put(
                    atmData.creditAccount,
                    creditCount.getOrDefault(atmData.creditAccount, 0) + 1
            );

            debitCount.put(
                    atmData.debitAccount,
                    debitCount.getOrDefault(atmData.debitAccount, 0) + 1
            );
        }

        return accounts
                .entrySet()
                .stream()
                .map(entry -> new AccountBalance(
                                entry.getKey(),
                                entry.getValue(),
                                creditCount.getOrDefault(entry.getKey(), 0),
                                debitCount.getOrDefault(entry.getKey(), 0)
                        )
                ).sorted((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.account, o2.account))
                        .toList();

    }

}
