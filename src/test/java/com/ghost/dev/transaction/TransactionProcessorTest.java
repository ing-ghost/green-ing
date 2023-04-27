package com.ghost.dev.transaction;

import com.fasterxml.jackson.core.JsonFactory;
import com.ghost.dev.json.JacksonStreamFactory;
import com.ghost.dev.json.SerializationFactory;
import com.ghost.dev.processor.ArrayDataInputStream;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.ghost.dev.transaction.model.AccountBalance;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.ghost.dev.processor.DataProcessorExecutor.processData;

public class TransactionProcessorTest {

    private final SerializationFactory serializationFactory = new JacksonStreamFactory(new JsonFactory());

    @Test
    void testStatic() throws IOException {
        List<AccountBalance> accountBalances = processData(
                new TransactionProcessor(),
                new EmptyDataProcessorConfig(),
                new ArrayDataInputStream<>(
                        new TransactionTestData().staticData()
                ),
                input -> input
        );

        System.out.println(
                new String(serializationFactory.transactionSerializer().serialize(accountBalances))
        );
    }

    @Test
    void testDynamic() {
        processData(
                new TransactionProcessor(),
                new EmptyDataProcessorConfig(),
                new ArrayDataInputStream<>(
                        new TransactionTestData().testData(1_000_000, 4_000_000)
                ),
                input -> input
        );
    }

}
