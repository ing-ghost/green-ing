package com.ghost.dev.game;

import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;
import com.ghost.dev.processor.ArrayDataInputStream;
import com.ghost.dev.processor.config.GameDataProcessorConfig;
import org.junit.jupiter.api.Test;

import static com.ghost.dev.processor.DataProcessorExecutor.processData;

public class GreenClanDataProcessorTest {

    @Test
    void testStatic() {
        ClanJob clanJob = new ClanTestData().staticTestData();

        processData(
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
