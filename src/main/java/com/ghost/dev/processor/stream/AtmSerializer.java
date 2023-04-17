package com.ghost.dev.processor.stream;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.network.serializer.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class AtmSerializer implements Serializer<List<AtmData>> {

    private final JsonFactory jFactory;

    public AtmSerializer(JsonFactory jFactory) {
        this.jFactory = jFactory;
    }

    @Override
    public String serialize(List<AtmData> result) throws IOException {
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

        stream.close();

        String out = stream.toString("UTF-8");
        generator.close();
        return out;
    }
}
