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

public final class TransactionSerializer implements Serializer<List<AccountBalance>> {

    private final JsonFactory jFactory;

    public TransactionSerializer(JsonFactory jFactory) {
        this.jFactory = jFactory;
    }

    @Override
    public byte[] serialize(List<AccountBalance> result) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        JsonGenerator generator = jFactory
                .createGenerator(stream, JsonEncoding.UTF8);

        generator.writeStartArray();

        for(AccountBalance accountBalance : result) {
            generator.writeStartObject();

            generator.writeStringField("account", accountBalance.account);
            generator.writeNumberField("debitCount", accountBalance.debitCount);
            generator.writeNumberField("creditCount", accountBalance.creditCount);
            generator.writeNumberField("balance", accountBalance.amount);

            generator.writeEndObject();
        }

        generator.writeEndArray();
        generator.flush();

        generator.close();
        stream.close();

        return stream.toByteArray();
    }
}