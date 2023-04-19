package com.ghost.dev.json.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.processor.config.GameDataProcessorConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GameDeserializer implements Deserialize<GameDataProcessorConfig, ClanData[]> {

    private final JsonFactory jFactory;

    public GameDeserializer(JsonFactory jFactory) {
        this.jFactory = jFactory;
    }

    @Override
    public Request<GameDataProcessorConfig, ClanData[]> deserialize(InputStream inputStream) throws IOException {
        JsonParser jParser = jFactory.createParser(inputStream);

        List<ClanData> clanList = new ArrayList<>();
        int groupCount = -1;

        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jParser.currentName();
            if ("groupCount".equals(fieldName)) {
                jParser.nextToken();
                groupCount = jParser.getIntValue();
            }
            if ("clans".equals(fieldName)) {
                jParser.nextToken();

                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                    int numberOfPlayers = -1;
                    int points = -1;
                    while (jParser.nextToken() != JsonToken.END_OBJECT) {
                        fieldName = jParser.currentName();
                        if ("numberOfPlayers".equals(fieldName)) {
                            jParser.nextToken();
                            numberOfPlayers = jParser.getIntValue();
                        }
                        if ("points".equals(fieldName)) {
                            jParser.nextToken();
                            points = jParser.getIntValue();
                        }
                    }
                    clanList.add(new ClanData(numberOfPlayers, points));
                }
            }
        }

        jParser.close();

        return new Request<>(new GameDataProcessorConfig(groupCount), clanList.toArray(ClanData[]::new));
    }
}