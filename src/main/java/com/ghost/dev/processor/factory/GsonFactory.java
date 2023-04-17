package com.ghost.dev.processor.factory;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Serializer;
import com.ghost.dev.processor.JsonFactory;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.ghost.dev.processor.config.GameDataProcessorConfig;
import com.ghost.dev.processor.gson.ArrayGsonDeserializer;
import com.ghost.dev.processor.gson.GameGsonDeserializer;
import com.ghost.dev.processor.gson.GsonSerializer;
import com.ghost.dev.processor.jackson.ArrayJacksonDeserializer;
import com.ghost.dev.processor.jackson.GameJacksonDeserializer;
import com.ghost.dev.processor.jackson.JacksonSerializer;
import com.ghost.dev.transaction.model.AccountBalance;
import com.ghost.dev.transaction.model.TransactionData;
import com.google.gson.Gson;

import java.util.List;

public class GsonFactory implements JsonFactory {

    private final Gson gson;

    public GsonFactory(Gson gson) {
        this.gson = gson;
    }


    @Override
    public Serializer<List<AtmData>> atmSerializer() {
        return new GsonSerializer<>(gson);
    }

    @Override
    public Serializer<List<AccountBalance>> transactionSerializer() {
        return new GsonSerializer<>(gson);
    }

    @Override
    public Serializer<List<List<ClanData>>> gameSerializer() {
        return new GsonSerializer<>(gson);
    }

    @Override
    public Deserialize<EmptyDataProcessorConfig, AtmData[]> atmDeserializer() {
        return new ArrayGsonDeserializer<>(gson, AtmData.class);
    }

    @Override
    public Deserialize<EmptyDataProcessorConfig, TransactionData[]> transactionDeserializer() {
        return new ArrayGsonDeserializer<>(gson, TransactionData.class);
    }

    @Override
    public Deserialize<GameDataProcessorConfig, ClanData[]> gameDeserializer() {
        return new GameGsonDeserializer(gson);
    }
}
