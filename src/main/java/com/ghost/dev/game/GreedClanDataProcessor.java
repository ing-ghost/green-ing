package com.ghost.dev.game;

import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class GreedClanDataProcessor implements DataProcessor<ClanData, List<List<ClanData>>> {

    final int groupSize;

    public GreedClanDataProcessor(int groupSize) {
        this.groupSize = groupSize;
    }

    @Override
    public List<List<ClanData>> processData(DataInputStream<ClanData> dataStream) {
        List<ClanData> clans = fetchDataAndSort(dataStream);

        final List<List<ClanData>> results = new ArrayList<>();

        List<ClanData> group = new LinkedList<>();
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
        List<ClanData> list = new LinkedList<>();

        for (ClanData clanData : dataStream) {
            list.add(clanData);
        }

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
