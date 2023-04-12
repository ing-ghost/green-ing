package com.ghost.dev.game.model;

public final class ClanJob {
    public final int groupCount;
    public final ClanData[] clans;

    public ClanJob() {
        groupCount = 0;
        clans = null;
    }

    public ClanJob(int groupCount, ClanData[] clanData) {
        this.groupCount = groupCount;
        this.clans = clanData;
    }
}
