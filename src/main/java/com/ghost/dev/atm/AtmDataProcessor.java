package com.ghost.dev.atm;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmStatus;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class AtmDataProcessor implements DataProcessor<AtmData, List<AtmData>> {

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
        List<AtmData> list = new LinkedList<>();

        for(AtmData atmData : dataStream) {
            list.add(atmData);
        }

        list.sort((o1, o2) -> {
            int s1 = score(o1);
            int s2 = score(o2);
            int cmp = Integer.compare(s1, s2);
            if (cmp == 0) {
                return Integer.compare(o1.atmId, o2.atmId);
            } else {
                return cmp;
            }
        });


        List<AtmData> result = new ArrayList<>(list.size());
        int region = -1;
        List<Integer> atm = new ArrayList<>();

        for(AtmData atmData : list) {
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

//    @Override
//    public List<AtmData> processData(DataInputStream<AtmData> atmDataStream) {
//        System.out.println("Start process free mem: " + Runtime.getRuntime().freeMemory());
//        Map<AtmData, Integer> scoredMap = new HashMap<>();
//
//        for(AtmData atmData : atmDataStream) {
//            Integer old = scoredMap.get(atmData);
//            int newScore = score(atmData);
//            if (old == null || old > newScore) {
//                scoredMap.put(atmData, newScore);
//            }
//        }
//
//        System.out.println("sorting process free mem: " + Runtime.getRuntime().freeMemory());
//
//        List<Map.Entry<AtmData, Integer>> scoredList;
//
//        scoredList = new ArrayList<>(scoredMap.entrySet());
//
//        scoredList.sort(Comparator.comparingInt(Map.Entry::getValue));
//
//        return scoredList.stream().map(Map.Entry::getKey).toList();
//    }

}
