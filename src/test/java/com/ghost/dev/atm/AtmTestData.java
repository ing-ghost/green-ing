package com.ghost.dev.atm;

import com.ghost.dev.Resources;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ghost.dev.atm.model.AtmStatus.FAILURE_RESTART;
import static com.ghost.dev.atm.model.AtmStatus.PRIORITY;
import static com.ghost.dev.atm.model.AtmStatus.SIGNAL_LOW;
import static com.ghost.dev.atm.model.AtmStatus.STANDARD;

public final class AtmTestData {
    
    public AtmData[] staticTestData() {
        return new Resources().loadArray(Resources.ATM_REQUEST_1, AtmData.class, AtmView.Request.class);
    }

    public AtmData[] generateTestData(
            int regions, int atmMin, int atmMax
    ) {
        List<AtmData> data = new ArrayList<>();
        Random random = new Random();

        for(int i = 1; i<regions+1; i++) {
            int atmCount = random.nextInt(atmMin, atmMax + 1);
            for(int j = 0; j<atmCount; j++) {
                data.add(new AtmData(i, randomRequest(random), j));
            }
        }

        return data.toArray(new AtmData[0]);
    }

    private String randomRequest(Random random) {
        int index = random.nextInt(4);
        return switch (index) {
            case 0 -> STANDARD;
            case 1 -> PRIORITY;
            case 2 -> SIGNAL_LOW;
            case 3 -> FAILURE_RESTART;
            default -> STANDARD;
        };
    }
}
