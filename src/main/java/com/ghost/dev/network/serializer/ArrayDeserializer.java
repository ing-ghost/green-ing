package com.ghost.dev.network.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;

import java.io.IOException;
import java.io.InputStream;

public class ArrayDeserializer<D> implements Deserialize<EmptyDataProcessorConfig, D[]> {

    private final ObjectMapper objectMapper;
    private Class<D> clazz;

    public ArrayDeserializer(ObjectMapper objectMapper, Class<D> clazz) {
        this.objectMapper = objectMapper;
        this.clazz = clazz;
    }

    @Override
    public Request<EmptyDataProcessorConfig, D[]> deserialize(InputStream inputStream) throws IOException {
        D[] data = (D[])objectMapper.readValue(inputStream, clazz.arrayType());
        return new Request<>(new EmptyDataProcessorConfig(), data);
    }
}
