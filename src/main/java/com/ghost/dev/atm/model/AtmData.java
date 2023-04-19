package com.ghost.dev.atm.model;

import java.util.Objects;

public final class AtmData {

    public final int region;

    public final int requestType;

    public final int atmId;

    public AtmData(int region, int requestType, int atmId) {
        this.region = region;
        this.requestType = requestType;
        this.atmId = atmId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtmData atmData = (AtmData) o;
        return region == atmData.region && requestType == atmData.requestType && atmId == atmData.atmId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, requestType, atmId);
    }

    @Override
    public String toString() {
        return "(" + region + ", " + requestType +", " + atmId + ")";
    }
}
