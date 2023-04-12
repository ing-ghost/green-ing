package com.ghost.dev.game;

import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.processor.DataInputStream;
import com.ghost.dev.processor.DataProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class KnapsackClanDataProcessor implements DataProcessor<ClanData, List<List<ClanData>>> {

    final int groupSize;

    public KnapsackClanDataProcessor(int groupSize) {
        this.groupSize = groupSize;
    }

    private void resetKnapsack(int[] k, int groupSize, int clanCount) {
        for (int i = 0; i <= groupSize; i++) {
            k[i] = 0;
        }

        for (int i = 0; i <= clanCount; i++) {
            k[i * (groupSize + 1)] = 0;
        }
    }

    private void solveKnapsack(List<ClanData> clans, int[] k, int groupSize, int clanCount) {
        for (int i = 1; i <= clanCount; i++) {
            for (int w = 1; w <= groupSize; w++) {
                int wt = clans.get(i - 1).numberOfPlayers;
                int val = 0;
                if (wt <= w) {
                    int offset = (i - 1) * (groupSize + 1);
                    val = Math.max(
                            clans.get(i - 1).points + k[offset + w - wt],
                            k[offset + w]
                    );
                } else {
                    val = k[(i - 1) * (groupSize + 1) + w];
                }
                k[i * (groupSize + 1) + w] = val;
            }
        }
    }

    private List<ClanData> collectClans(List<ClanData> clans, int[] k, int groupSize, int clanCount) {
        int res = k[clanCount * (groupSize + 1) + groupSize];

        List<ClanData> result = new ArrayList<>();

        int w = groupSize;
        for (int i = clanCount; i > 0 && res > 0; i--) {
            if (res != k[(i - 1) * (groupSize + 1) + w]) {
                ClanData clanData = clans.get(i - 1);
                result.add(clanData);
                res -= clanData.points;
                w -= clanData.numberOfPlayers;
            }
        }

        return result;
    }

    @Override
    public List<List<ClanData>> processData(DataInputStream<ClanData> dataStream) {
        List<ClanData> clans = fetchData(dataStream);

        if (clans.size() == 0) return Collections.emptyList();

        // groupSize => w
        // clanCount = n
        //  [x][y] => x * (groupSize + 1) + y
        // [0..clanCount+1] [0..groupSize + 1]

        // alloc for biggest knapsack
        int[] k = new int[(groupSize + 1) * (clans.size() + 1)];

        List<List<ClanData>> result = new ArrayList<>();

        while (!clans.isEmpty()) {
            int clanCount = clans.size();
            resetKnapsack(k, groupSize, clanCount);
            solveKnapsack(clans, k, groupSize, clanCount);
            List<ClanData> subResult = collectClans(clans, k, groupSize, clanCount);

            result.add(subResult);

            clans.removeAll(subResult);
        }

        return result;
    }

    private List<ClanData> fetchData(DataInputStream<ClanData> dataStream) {
        List<ClanData> list = new ArrayList<>();

        for (ClanData clanData : dataStream) {
            list.add(clanData);
        }

        return list;
    }

}
