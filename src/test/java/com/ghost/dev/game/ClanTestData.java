package com.ghost.dev.game;

import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;

import java.util.Random;

public class ClanTestData {

    public final ClanJob staticTestData() {
        return new ClanJob(6,
                new ClanData[] {
                        new ClanData(4, 50),  // 12
                        new ClanData(2, 70),  // 35 (!)
                        new ClanData(6, 60),  // 10
                        new ClanData(1, 15),  // 15
                        new ClanData(5, 40),  //  8
                        new ClanData(3, 45),  // 15
                        new ClanData(1, 12),  // 12
                        new ClanData(4, 40),  // 10
                }
        );
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
