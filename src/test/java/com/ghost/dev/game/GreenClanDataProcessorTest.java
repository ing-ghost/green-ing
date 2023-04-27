package com.ghost.dev.game;

import com.fasterxml.jackson.core.JsonFactory;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;
import com.ghost.dev.json.JacksonStreamFactory;
import com.ghost.dev.json.SerializationFactory;
import com.ghost.dev.processor.ArrayDataInputStream;
import com.ghost.dev.processor.config.GameDataProcessorConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.ghost.dev.processor.DataProcessorExecutor.processData;

public class GreenClanDataProcessorTest {

    private final SerializationFactory serializationFactory = new JacksonStreamFactory(new JsonFactory());

    @Test
    void testStatic() throws IOException {
        ClanJob clanJob = new ClanTestData().staticTestData();

        List<List<ClanData>> result = processData(
                new GreedClanDataProcessor(),
                new GameDataProcessorConfig(clanJob.groupCount),
                new ArrayDataInputStream<>(clanJob.clans),
                input -> {
                    for (ClanData clanData : input) {
                        System.out.print("(" + clanData.points + ", " + clanData.numberOfPlayers + ")");
                    }
                    System.out.println();
                    return input;
                }
        );

        System.out.println(
                new String(serializationFactory.gameSerializer().serialize(result))
        );
    }

    @Test
    void testDynamic() {
        for(int i =0; i<3; i++) {
            ClanJob clanJob = new ClanTestData()
                    .dynamicData(
                            1000,
                            20000,
                            98, 99
                    );

            processData(
                    new GreedClanDataProcessor(),
                    new GameDataProcessorConfig(clanJob.groupCount),
                    new ArrayDataInputStream<>(clanJob.clans),
                    input -> input
            );
        }
    }

}
