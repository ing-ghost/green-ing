package com.ghost.dev.processor.gson;

import com.ghost.dev.network.serializer.Serializer;
import com.google.gson.Gson;

import java.io.IOException;

public class GsonSerializer<E> implements Serializer<E> {

    private final Gson gson;

    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }


    @Override
    public String serialize(E result) throws IOException {
        return gson.toJson(result);
    }
}
