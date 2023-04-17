package com.ghost.dev.processor.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Serializer;
import com.ghost.dev.processor.JsonFactory;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.ghost.dev.processor.config.GameDataProcessorConfig;
import com.ghost.dev.processor.jackson.ArrayJacksonDeserializer;
import com.ghost.dev.processor.jackson.GameJacksonDeserializer;
import com.ghost.dev.processor.jackson.JacksonSerializer;
import com.ghost.dev.transaction.model.AccountBalance;
import com.ghost.dev.transaction.model.TransactionData;

import java.util.List;

public class JacksonFactory implements JsonFactory {

    private final ObjectMapper objectMapper;

    public JacksonFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Serializer<List<AtmData>> atmSerializer() {
        return new JacksonSerializer<>(objectMapper);
    }

    @Override
    public Serializer<List<AccountBalance>> transactionSerializer() {
        return new JacksonSerializer<>(objectMapper);
    }

    @Override
    public Serializer<List<List<ClanData>>> gameSerializer() {
        return new JacksonSerializer<>(objectMapper);
    }

    @Override
    public Deserialize<EmptyDataProcessorConfig, AtmData[]> atmDeserializer() {
        return new ArrayJacksonDeserializer<>(objectMapper, AtmData.class);
    }

    @Override
    public Deserialize<EmptyDataProcessorConfig, TransactionData[]> transactionDeserializer() {
        return new ArrayJacksonDeserializer<>(objectMapper, TransactionData.class);
    }

    @Override
    public Deserialize<GameDataProcessorConfig, ClanData[]> gameDeserializer() {
        return new GameJacksonDeserializer(objectMapper);
    }
}
