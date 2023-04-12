package com.ghost.dev.atm;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmStatus;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * already slower than AtmDataProcessor and not finish
 */
public final class AtmDataProcessorQueue implements DataProcessor<AtmData, List<AtmData>> {

    PriorityQueue<AtmData> queue = new PriorityQueue<>(10_000, (o1, o2) -> {
        int s1 = score(o1);
        int s2 = score(o2);
        int cmp = Integer.compare(s1, s2);
        if (cmp == 0) {
            return Integer.compare(o1.atmId, o2.atmId);
        } else {
            return cmp;
        }
    });

    private int score(AtmData atmData) {
        return atmData.region * 5 + score(atmData.requestType);
    }

    private int score(String requestType) {
        return switch (requestType) {
            case AtmStatus.FAILURE_RESTART -> 0;
            case AtmStatus.PRIORITY -> 1;
            case AtmStatus.SIGNAL_LOW -> 2;
            case AtmStatus.STANDARD -> 3;
            default -> 4;
        };
    }

    @Override
    public List<AtmData> processData(DataInputStream<AtmData> dataStream) {
        for(AtmData atmData : dataStream) {
            queue.add(atmData);
        }

        List<AtmData> result = new ArrayList<>(queue.size());
        int region = -1;
        List<Integer> atm = new ArrayList<>();

        while(!queue.isEmpty()) {
            AtmData atmData = queue.poll();
            if (atmData.region != region) {
                atm.clear();
                region = atmData.region;
            }
            if (!atm.contains(atmData.atmId)) {
                atm.add(atmData.atmId);
                result.add(atmData);
            }
        }

        return result;
    }

}
