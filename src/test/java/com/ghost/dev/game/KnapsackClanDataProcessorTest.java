package com.ghost.dev.game;

import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;
import com.ghost.dev.processor.ArrayDataInputStream;
import com.ghost.dev.processor.config.GameDataProcessorConfig;
import org.junit.jupiter.api.Test;

import static com.ghost.dev.processor.DataProcessorExecutor.processData;

public class KnapsackClanDataProcessorTest {

    @Test
    void testStatic() {
        ClanJob clanJob = new ClanTestData().staticTestData();

        processData(
                new KnapsackClanDataProcessor(),
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
        ClanJob clanJob = new ClanTestData()
                .dynamicData(
                        1000,
                        1000,
                        1, 1000
                );

        processData(
                new KnapsackClanDataProcessor(),
                new GameDataProcessorConfig(clanJob.groupCount),
                new ArrayDataInputStream<>(clanJob.clans),
                input -> input
        );
    }

}
