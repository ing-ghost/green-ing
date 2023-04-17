package com.ghost.dev.processor.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;

import java.io.IOException;
import java.io.InputStream;

public class ArrayJacksonDeserializer<D> implements Deserialize<EmptyDataProcessorConfig, D[]> {

    private final ObjectMapper objectMapper;
    private Class<D> clazz;

    public ArrayJacksonDeserializer(ObjectMapper objectMapper, Class<D> clazz) {
        this.objectMapper = objectMapper;
        this.clazz = clazz;
    }

    @Override
    public Request<EmptyDataProcessorConfig, D[]> deserialize(InputStream inputStream) throws IOException {
        D[] data = (D[])objectMapper.readValue(inputStream, clazz.arrayType());
        return new Request<>(new EmptyDataProcessorConfig(), data);
    }
}
