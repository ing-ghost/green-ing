package com.ghost.dev.processor.gson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ArrayGsonDeserializer<D> implements Deserialize<EmptyDataProcessorConfig, D[]> {
    private Class<D> clazz;
    private final Gson gson;

    public ArrayGsonDeserializer(Gson gson, Class<D> clazz) {
        this.gson = gson;
        this.clazz = clazz;
    }

    @Override
    public Request<EmptyDataProcessorConfig, D[]> deserialize(InputStream inputStream) throws IOException {
        D[] data = (D[])gson.fromJson(new InputStreamReader(inputStream), clazz.arrayType());
        return new Request<>(new EmptyDataProcessorConfig(), data);
    }
}
