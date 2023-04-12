package com.ghost.dev.atm;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmStatus;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

public class AtmDataProcessorStream implements DataProcessor<AtmData, List<AtmData>> {
    @Override
    public List<AtmData> processData(DataInputStream<AtmData> dataStream) {
//        long start = System.currentTimeMillis();
        Map<Integer, List<AtmData>> byRegions = splitByRegions(dataStream);
//        System.out.println("Split time: " + (System.currentTimeMillis() - start));

//        start = System.currentTimeMillis();
        // Faster to split and then sort instead of using TreeMap
        List<AtmData> input = byRegions.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .flatMap((Function<Map.Entry<Integer, List<AtmData>>, Stream<AtmData>>) integerListEntry -> integerListEntry.getValue().stream())
                .toList();
//        System.out.println("sort time: " + (System.currentTimeMillis() - start));

        int failureCount = 0;
        int priorityCount = 0;
        int lowCount = 0;
        int standard = 0;

        int region = -1;
        int regionOffset = 0;

        List<AtmData> out = new ArrayList<>();

//        start = System.currentTimeMillis();
        for(AtmData atmData : input) {
            if (region != atmData.region) {
                region = atmData.region;
                failureCount = 0;
                priorityCount = 0;
                lowCount = 0;
                standard = 0;
                regionOffset = out.size();
            }

            int posInRegion = switch (atmData.requestType) {
                case AtmStatus.FAILURE_RESTART -> failureCount++;
                case AtmStatus.PRIORITY -> failureCount + (priorityCount++);
                case AtmStatus.SIGNAL_LOW -> failureCount + priorityCount + (lowCount++);
                default -> failureCount + priorityCount + lowCount + (standard++);
            };

            out.add(regionOffset + posInRegion, atmData);
        }
//        System.out.println("priority time: " + (System.currentTimeMillis() - start));

        List<AtmData> result = new ArrayList<>(out.size());
        region = -1;
        Set<Integer> atm = new HashSet<>();

//        start = System.currentTimeMillis();
        for(AtmData atmData : out) {
            if (atmData.region != region) {
                atm.clear();
                region = atmData.region;
            }
            if (!atm.contains(atmData.atmId)) {
                atm.add(atmData.atmId);
                result.add(atmData);
            }
        }

//        System.out.println("filter time: " + (System.currentTimeMillis() - start));

        return result;
    }

    private Map<Integer, List<AtmData>> splitByRegions(DataInputStream<AtmData> dataStream) {
        Map<Integer, List<AtmData>> split = new HashMap<>();

        for(AtmData atmData : dataStream) {
            List<AtmData> regionList = split.get(atmData.region);
            // computeIfAbsent has performance hit
            if (regionList == null) {
                regionList = new LinkedList<>();
                split.put(atmData.region, regionList);
            }
            regionList.add(atmData);
        }

        return split;
    }
}
