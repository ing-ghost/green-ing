package com.ghost.dev.json;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.json.stream.AtmDeserializer;
import com.ghost.dev.json.stream.AtmSerializer;
import com.ghost.dev.json.stream.GameDeserializer;
import com.ghost.dev.json.stream.GameSerializer;
import com.ghost.dev.json.stream.TransactionDeserializer;
import com.ghost.dev.json.stream.TransactionSerializer;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Serializer;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.ghost.dev.processor.config.GameDataProcessorConfig;
import com.ghost.dev.transaction.model.AccountBalance;
import com.ghost.dev.transaction.model.TransactionData;

import java.util.List;

public class JacksonStreamFactory implements JsonFactory {

    private final com.fasterxml.jackson.core.JsonFactory jsonFactory;

    public JacksonStreamFactory(com.fasterxml.jackson.core.JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }


    @Override
    public Serializer<List<AtmData>> atmSerializer() {
        return new AtmSerializer(jsonFactory);
    }

    @Override
    public Serializer<List<AccountBalance>> transactionSerializer() {
        return new TransactionSerializer(jsonFactory);
    }

    @Override
    public Serializer<List<List<ClanData>>> gameSerializer() {
        return new GameSerializer(jsonFactory);
    }

    @Override
    public Deserialize<EmptyDataProcessorConfig, AtmData[]> atmDeserializer() {
        return new AtmDeserializer(jsonFactory);
    }

    @Override
    public Deserialize<EmptyDataProcessorConfig, TransactionData[]> transactionDeserializer() {
        return new TransactionDeserializer(jsonFactory);
    }

    @Override
    public Deserialize<GameDataProcessorConfig, ClanData[]> gameDeserializer() {
        return new GameDeserializer(jsonFactory);
    }
}
