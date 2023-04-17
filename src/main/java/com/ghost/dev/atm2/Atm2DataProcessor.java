package com.ghost.dev.atm2;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmStatus;
import com.ghost.dev.atm2.model.AtmData2;
import com.ghost.dev.atm2.model.AtmStatus2;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;

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

public class Atm2DataProcessor implements DataProcessor<EmptyDataProcessorConfig, AtmData2, List<AtmData2>> {

    @Override
    public List<AtmData2> processData(EmptyDataProcessorConfig config, DataInputStream<AtmData2> dataStream) {
        Map<Integer, List<AtmData2>> byRegions = splitByRegions(dataStream);

        // Faster to split and then sort instead of using TreeMap
        List<AtmData2> input = sortByRegionAndFlatMap(byRegions);

        List<AtmData2> priority = sortAtmByPriority(input);

        return removeDuplicates(priority);
    }

    private List<AtmData2> removeDuplicates(List<AtmData2> priority) {
        List<AtmData2> result = new ArrayList<>(priority.size());
        int region = -1;
        Set<Integer> atm = new HashSet<>();

        for(AtmData2 atmData : priority) {
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

    private List<AtmData2> sortAtmByPriority(List<AtmData2> input) {
        int failureCount = 0;
        int priorityCount = 0;
        int lowCount = 0;
        int standard = 0;

        int region = -1;
        int regionOffset = 0;

        List<AtmData2> out = new ArrayList<>();

        List<AtmData2>[] priorityList = new List[] {
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        };

        for(AtmData2 atmData : input) {
            if (region != atmData.region) {
                region = atmData.region;

                for (List<AtmData2> pl : priorityList) {
                    out.addAll(regionOffset, pl);
                    pl.clear();
                }

                regionOffset = out.size();
            }

            priorityList[atmData.requestType].add(atmData);
        }

        for (List<AtmData2> pl : priorityList) {
            out.addAll(regionOffset, pl);
        }

        return out;
    }

    private List<AtmData2> sortByRegionAndFlatMap(Map<Integer, List<AtmData2>> byRegions) {
        return byRegions.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .flatMap((Function<Map.Entry<Integer, List<AtmData2>>, Stream<AtmData2>>) integerListEntry -> integerListEntry.getValue().stream())
                .toList();
    }

    private Map<Integer, List<AtmData2>> splitByRegions(DataInputStream<AtmData2> dataStream) {
        Map<Integer, List<AtmData2>> split = new HashMap<>();

        for(AtmData2 atmData : dataStream) {
            List<AtmData2> regionList = split.get(atmData.region);
            // computeIfAbsent has performance hit
            if (regionList == null) {
                regionList = new ArrayList<>();
                split.put(atmData.region, regionList);
            }
            regionList.add(atmData);
        }

        return split;
    }
}
