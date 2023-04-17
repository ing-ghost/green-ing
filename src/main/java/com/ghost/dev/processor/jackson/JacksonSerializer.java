package com.ghost.dev.processor.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.network.serializer.Serializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class JacksonSerializer<E> implements Serializer<E> {

    private final ObjectMapper objectMapper;

    public JacksonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(E result) throws IOException {
        return objectMapper.writeValueAsString(result);
    }
}
