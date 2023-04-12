package com.ghost.dev.game.model;

public final class ClanJob {
    public final int groupSize;
    public final ClanData[] clanData;

    public ClanJob(int groupSize, ClanData[] clanData) {
        this.groupSize = groupSize;
        this.clanData = clanData;
    }
}
