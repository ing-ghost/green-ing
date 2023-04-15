package com.ghost.dev.game;

import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;
import com.ghost.dev.processor.config.GameDataProcessorConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class GreedClanDataProcessor implements DataProcessor<GameDataProcessorConfig, ClanData, List<List<ClanData>>> {

    @Override
    public List<List<ClanData>> processData(GameDataProcessorConfig config, DataInputStream<ClanData> dataStream) {
        List<ClanData> clans = fetchDataAndSort(dataStream);

        int groupSize = config.groupCount;

        final List<List<ClanData>> results = new ArrayList<>();

        List<ClanData> group = new ArrayList<>();
        int size = 0;

        while(!clans.isEmpty()) {
            Iterator<ClanData> iter = clans.iterator();
            while (iter.hasNext() && size < groupSize) {
                ClanData clanData = iter.next();
                if (size + clanData.numberOfPlayers <= groupSize) {
                    iter.remove();
                    group.add(clanData);
                    size += clanData.numberOfPlayers;
                }
            }

            results.add(group);

            group = new ArrayList<>();
            size = 0;

        }

        return results;
    }

    private List<ClanData> fetchDataAndSort(DataInputStream<ClanData> dataStream) {
        List<ClanData> list = new ArrayList<>();

        long  start = System.currentTimeMillis();
        for (ClanData clanData : dataStream) {
            list.add(clanData);
        }
        System.out.println("COPY: " + (System.currentTimeMillis() - start));

        list.sort((o1, o2) -> {
            int result = Integer.compare(o2.points, o1.points);
            if (result == 0) {
                return Integer.compare(o1.numberOfPlayers, o2.numberOfPlayers);
            } else {
                return result;
            }
        });

        return list;
    }

}
