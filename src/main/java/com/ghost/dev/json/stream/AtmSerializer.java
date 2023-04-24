package com.ghost.dev.json.stream;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.network.serializer.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public final class AtmSerializer implements Serializer<List<AtmData>> {

    private final JsonFactory jFactory;

    public AtmSerializer(JsonFactory jFactory) {
        this.jFactory = jFactory;
    }

    @Override
    public byte[] serialize(List<AtmData> result) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        JsonGenerator generator = jFactory
                .createGenerator(stream, JsonEncoding.UTF8);

        generator.writeStartArray();

        for(AtmData atmData : result) {
            generator.writeStartObject();
            generator.writeNumberField("region", atmData.region);
            generator.writeNumberField("atmId", atmData.atmId);
            generator.writeEndObject();
        }

        generator.writeEndArray();
        generator.flush();

        generator.close();
        stream.close();

        return stream.toByteArray();
    }
}