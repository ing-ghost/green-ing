package com.ghost.dev.atm.model;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Objects;

public final class AtmData {

    @JsonView(AtmView.Normal.class)
    public final int region;

    @JsonView(AtmView.Request.class)
    public final String requestType;

    @JsonView(AtmView.Normal.class)
    public final int atmId;

    public AtmData() {
        region = -1;
        requestType = null;
        atmId = -1;
    }

    public AtmData(int region, String requestType, int atmId) {
        this.region = region;
        this.requestType = requestType;
        this.atmId = atmId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtmData atmData = (AtmData) o;
        return region == atmData.region && atmId == atmData.atmId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, atmId);
    }

    @Override
    public String toString() {
        return "(" + region + ", " + requestType + ", " + atmId + ")";
    }
}
