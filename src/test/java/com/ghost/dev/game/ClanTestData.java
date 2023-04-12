package com.ghost.dev.game;

import com.ghost.dev.Resources;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.game.model.ClanJob;

import java.util.Random;

public class ClanTestData {

    public final ClanJob staticTestData() {
        return new Resources().loadObject(Resources.GAME_REQUEST_1, ClanJob.class);
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
