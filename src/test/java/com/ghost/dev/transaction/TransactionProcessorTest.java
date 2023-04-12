package com.ghost.dev.transaction;

import com.ghost.dev.processor.ArrayDataInputStream;
import org.junit.jupiter.api.Test;

import static com.ghost.dev.processor.DataProcessorExecutor.processData;

public class TransactionProcessorTest {

    @Test
    void testStatic() {
        processData(
                new TransactionProcessor(),
                new ArrayDataInputStream<>(
                        new TransactionTestData().staticData()
                ),
                input -> {
                    System.out.println(input.account + " - " + input.amount +
                            " | " + input.creditCount + ", " + input.debitCount);
                    return input;
                }
        );
    }

    @Test
    void testDynamic() {
        processData(
                new TransactionProcessor(),
                new ArrayDataInputStream<>(
                        new TransactionTestData().testData(1_000_000, 4_000_000)
                ),
                input -> input
        );
    }

}
