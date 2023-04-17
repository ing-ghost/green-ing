package com.ghost.dev.processor.gson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.processor.config.GameDataProcessorConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameGsonDeserializer implements Deserialize<GameDataProcessorConfig, ClanData[]> {

    private final Gson gson;

    public GameGsonDeserializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Request<GameDataProcessorConfig, ClanData[]> deserialize(InputStream inputStream) throws IOException {
        ClanJob data = gson.fromJson(new InputStreamReader(inputStream), ClanJob.class);
        return new Request(new GameDataProcessorConfig(data.groupCount), data.clans);
    }
}
