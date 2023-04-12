package com.ghost.dev.game;

import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;
import com.ghost.dev.processor.ArrayDataInputStream;
import org.junit.jupiter.api.Test;

import static com.ghost.dev.processor.DataProcessorExecutor.processData;

public class GreenClanDataProcessorTest {

    @Test
    void testStatic() {
        ClanJob clanJob = new ClanTestData().staticTestData();

        processData(
                new GreedClanDataProcessor(clanJob.groupCount),
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
                        50000,
                        1, 100
                );

        processData(
                new GreedClanDataProcessor(clanJob.groupCount),
                new ArrayDataInputStream<>(clanJob.clans),
                input -> input
        );
    }

}
