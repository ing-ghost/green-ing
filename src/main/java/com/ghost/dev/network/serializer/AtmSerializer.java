package com.ghost.dev.network.serializer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.atm.model.AtmData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class AtmSerializer<E> implements Serializer<List<AtmData>> {

    private final JsonFactory jFactory = new JsonFactory();

    public AtmSerializer() { }

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

        return stream.toString("UTF-8");
    }
}
