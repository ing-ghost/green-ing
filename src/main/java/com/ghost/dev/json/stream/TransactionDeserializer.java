package com.ghost.dev.json.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.ghost.dev.transaction.model.TransactionData;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class TransactionDeserializer implements Deserialize<EmptyDataProcessorConfig, TransactionData[]> {

    private final JsonFactory jFactory;

    public TransactionDeserializer(JsonFactory jFactory) {
        this.jFactory = jFactory;
    }

    @Override
    public Request<EmptyDataProcessorConfig, TransactionData[]> deserialize(InputStream inputStream) throws IOException {
        JsonParser jParser = jFactory.createParser(inputStream);

        List<TransactionData> transactionList = new ArrayList<>();

        while (jParser.nextToken() != JsonToken.END_ARRAY) {
            String creditAccount = null;
            String debitAccount = null;
            BigDecimal amount = null;

            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jParser.currentName();
                if ("creditAccount".equals(fieldName)) {
                    jParser.nextToken();
                    creditAccount = jParser.getText();
                }
                if ("debitAccount".equals(fieldName)) {
                    jParser.nextToken();
                    debitAccount = jParser.getText();
                }
                if ("amount".equals(fieldName)) {
                    jParser.nextToken();
                    amount = jParser.getDecimalValue();
                }
            }

            transactionList.add(new TransactionData(creditAccount, debitAccount, amount));

        }

        jParser.close();

        return new Request<>(new EmptyDataProcessorConfig(), transactionList.toArray(TransactionData[]::new));
    }
}