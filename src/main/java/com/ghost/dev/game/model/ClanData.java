package com.ghost.dev.game.model;

public final class ClanData {
    public final int numberOfPlayers;
    public final int points;

    public ClanData() {
        numberOfPlayers = 0;
        points = 0;
    }

    public ClanData(int numberOfPlayers, int points) {
        this.numberOfPlayers = numberOfPlayers;
        this.points = points;
    }

}
