package com.ghost.dev.atm;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public final class AtmDataProcessor implements DataProcessor<EmptyDataProcessorConfig, AtmData, List<AtmData>> {

    @Override
    public List<AtmData> processData(EmptyDataProcessorConfig config, DataInputStream<AtmData> dataStream) {
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
        int region = -1;

        List<AtmData> out = new ArrayList<>();

        List<AtmData>[] priorityList = new List[] {
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        };

        for(AtmData atmData : input) {
            if (region != atmData.region) {
                region = atmData.region;

                for (List<AtmData> pl : priorityList) {
                    out.addAll(pl);
                    pl.clear();
                }
            }

            priorityList[atmData.requestType].add(atmData);
        }

        for (List<AtmData> pl : priorityList) {
            out.addAll(pl);
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
                regionList = new ArrayList<>();
                split.put(atmData.region, regionList);
            }
            regionList.add(atmData);
        }

        return split;
    }
}
