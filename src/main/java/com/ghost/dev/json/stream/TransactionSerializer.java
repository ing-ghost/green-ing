package com.ghost.dev.json.stream;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.ghost.dev.network.serializer.Serializer;
import com.ghost.dev.transaction.model.AccountBalance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TransactionSerializer implements Serializer<List<AccountBalance>> {

    private final JsonFactory jFactory;

    public TransactionSerializer(JsonFactory jFactory) {
        this.jFactory = jFactory;
    }

    @Override
    public String serialize(List<AccountBalance> result) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        JsonGenerator generator = jFactory
                .createGenerator(stream, JsonEncoding.UTF8);

        generator.writeStartArray();

        for(AccountBalance accountBalance : result) {
            generator.writeStartObject();

            generator.writeStringField("account", accountBalance.account);
            generator.writeStringField("amount", accountBalance.amount.toString());
            generator.writeNumberField("creditCount", accountBalance.creditCount);
            generator.writeNumberField("debitCount", accountBalance.debitCount);

            generator.writeEndObject();
        }

        generator.writeEndArray();

        generator.close();
        stream.close();

        return stream.toString(StandardCharsets.UTF_8);
    }
}