package com.ghost.dev.game;

import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;
import com.ghost.dev.processor.config.GameDataProcessorConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GreedClanDataProcessor implements DataProcessor<GameDataProcessorConfig, ClanData, List<List<ClanData>>> {

    @Override
    public List<List<ClanData>> processData(GameDataProcessorConfig config, DataInputStream<ClanData> dataStream) {
        ClanData[] clans = fetchDataAndSort(dataStream);

        int groupSize = config.groupCount;

        final List<List<ClanData>> results = new ArrayList<>();

        List<ClanData> group = new ArrayList<>();
        int size = 0;

        int removedCount = 0;
        while(removedCount < clans.length) {
            int index = 0;

            while (index < clans.length && size < groupSize) {
                ClanData clanData = clans[index];

                if (clanData != null && size + clanData.numberOfPlayers <= groupSize) {
                    removedCount++;
                    clans[index] = null;
                    group.add(clanData);
                    size += clanData.numberOfPlayers;
                }

                index++;
            }

            results.add(group);

            group = new ArrayList<>();
            size = 0;

        }

        return results;
    }

    private ClanData[] fetchDataAndSort(DataInputStream<ClanData> dataStream) {
        ClanData[] data = dataStream.getAll();

        Arrays.sort(data, (o1, o2) -> {
            int result = Integer.compare(o2.points, o1.points);
            if (result == 0) {
                return Integer.compare(o1.numberOfPlayers, o2.numberOfPlayers);
            } else {
                return result;
            }
        });

        return data;
    }

}
