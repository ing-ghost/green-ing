package com.ghost.dev.timer;

import java.util.ArrayList;
import java.util.List;

public class TimeTracker {

    List<Long> serializeTime = new ArrayList<>();
    List<Long> processTime = new ArrayList<>();
    List<Long> deserializeTime = new ArrayList<>();
    List<Long> total = new ArrayList<>();

    public synchronized void trackTime(long serializeTime, long processTime, long deserializeTime) {
        this.serializeTime.add(serializeTime);
        this.processTime.add(processTime);
        this.deserializeTime.add(deserializeTime);
        this.total.add(serializeTime + processTime + deserializeTime);
    }

    public synchronized void reset() {
        this.serializeTime.clear();
        this.processTime.clear();
        this.deserializeTime.clear();
        this.processTime.clear();
    }

    public synchronized String summary() {
        return "{\n" +
                "   \"count:\" " + this.processTime.size() + ", \n" +
                "   \"deserializeTime\": " + avg(deserializeTime) + ",\n" +
                "   \"serializeTime\": " + avg(serializeTime) + ",\n" +
                "   \"processTime\": " + avg(processTime) + ",\n" +
                "   \"totalTime\": " + avg(total) + "\n" +
                "}\n";
    }

    private long avg(List<Long> time) {
        if (time.size() == 0) return 0L;

        long sum = 0L;
        for(long value : time) {
            sum += value;
        }

        return sum / time.size();
    }
}

