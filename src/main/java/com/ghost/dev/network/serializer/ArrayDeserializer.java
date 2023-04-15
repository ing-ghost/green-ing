package com.ghost.dev.network.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ArrayDeserializer<D> implements Deserialize<EmptyDataProcessorConfig, D[]> {

    private final ObjectMapper objectMapper;
    private Class<D> clazz;
    private final Gson gson = new GsonBuilder().create();

    public ArrayDeserializer(ObjectMapper objectMapper, Class<D> clazz) {
        this.objectMapper = objectMapper;
        this.clazz = clazz;
    }

    @Override
    public Request<EmptyDataProcessorConfig, D[]> deserialize(InputStream inputStream) throws IOException {
//        D[] data = (D[])objectMapper.readValue(inputStream, clazz.arrayType());
        D[] data = (D[])gson.fromJson(new InputStreamReader(inputStream), clazz.arrayType());
        return new Request<>(new EmptyDataProcessorConfig(), data);
    }
}
