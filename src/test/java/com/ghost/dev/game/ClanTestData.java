package com.ghost.dev.game;

import com.ghost.dev.Resources;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;
import com.ghost.dev.json.JacksonStreamFactory;
import com.ghost.dev.json.JsonFactory;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.processor.config.GameDataProcessorConfig;

import java.util.Random;

public class ClanTestData {

    private final JsonFactory jsonFactory = new JacksonStreamFactory(new com.fasterxml.jackson.core.JsonFactory());

    public final ClanJob staticTestData() {
        Request<GameDataProcessorConfig, ClanData[]> request = new Resources()
                .loadObject(Resources.GAME_REQUEST_1, jsonFactory.gameDeserializer());

        return new ClanJob(request.config.groupCount, request.data);
    }

    public final ClanJob dynamicData(int groupSize, int clanCount, int minClanSize, int maxClanSize) {
        ClanData[] clanData = new ClanData[clanCount];
        Random random = new Random();
        for (int i = 0; i<clanCount; i++) {
            clanData[i] = new ClanData(
                    random.nextInt(minClanSize, maxClanSize + 1),
                    random.nextInt(10, 120)
            );
        }
        return new ClanJob(groupSize, clanData);
    }

}
