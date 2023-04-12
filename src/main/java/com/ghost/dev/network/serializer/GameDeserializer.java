package com.ghost.dev.network.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;
import com.ghost.dev.processor.config.GameDataProcessorConfig;

import java.io.IOException;
import java.io.InputStream;

public class GameDeserializer implements Deserialize<GameDataProcessorConfig, ClanData[]> {

    private final ObjectMapper objectMapper;

    public GameDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Request<GameDataProcessorConfig, ClanData[]> deserialize(InputStream inputStream) throws IOException {
        ClanJob data = objectMapper.readValue(inputStream, ClanJob.class);
        return new Request(new GameDataProcessorConfig(data.groupCount), data.clans);
    }
}
