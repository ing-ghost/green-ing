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
import java.util.function.Function;
import java.util.stream.Stream;

public class AtmDataProcessorStream implements DataProcessor<AtmData, List<AtmData>> {

    @Override
    public List<AtmData> processData(DataInputStream<AtmData> dataStream) {
        Map<Integer, List<AtmData>> byRegions = splitByRegions(dataStream);

        // Faster to split and then sort instead of using TreeMap
        List<AtmData> input = sortByRegionAndFlatMap(byRegions);

        List<AtmData> priority = sortAtmByPriority(input);

        return removeDuplicates(priority);
    }

    private List<AtmData> removeDuplicates(List<AtmData> priority) {
        List<AtmData> result = new ArrayList<>(priority.size());
        int region = -1;
        Set<Integer> atm = new HashSet<>();

        for(AtmData atmData : priority) {
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

    private List<AtmData> sortAtmByPriority(List<AtmData> input) {
        int failureCount = 0;
        int priorityCount = 0;
        int lowCount = 0;
        int standard = 0;

        int region = -1;
        int regionOffset = 0;

        List<AtmData> out = new ArrayList<>();

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

        return out;
    }

    private List<AtmData> sortByRegionAndFlatMap(Map<Integer, List<AtmData>> byRegions) {
        return byRegions.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .flatMap((Function<Map.Entry<Integer, List<AtmData>>, Stream<AtmData>>) integerListEntry -> integerListEntry.getValue().stream())
                .toList();
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
