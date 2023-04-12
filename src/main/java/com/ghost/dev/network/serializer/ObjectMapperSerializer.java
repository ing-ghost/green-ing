package com.ghost.dev.network.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectMapperSerializer<E> implements Serializer<E> {

    private final ObjectMapper objectMapper;

    public ObjectMapperSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(E result) throws IOException {
        return objectMapper.writeValueAsString(result);
    }
}
