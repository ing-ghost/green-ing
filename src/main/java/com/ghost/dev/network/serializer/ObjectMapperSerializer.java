package com.ghost.dev.network.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class ObjectMapperSerializer<E> implements Serializer<E> {

    private final ObjectMapper objectMapper;
    private final Gson gson = new GsonBuilder().create();

    public ObjectMapperSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(E result) throws IOException {
//        return objectMapper.writeValueAsString(result);
        return gson.toJson(result);
    }
}
