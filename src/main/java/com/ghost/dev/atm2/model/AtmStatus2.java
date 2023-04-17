package com.ghost.dev.atm2.model;

public final class AtmStatus2 {
    public static final int STANDARD = 0;
    public static final int PRIORITY = 1;
    public static final int SIGNAL_LOW = 2;
    public static final int FAILURE_RESTART = 3;

    public static int of(String status) {
        return switch (status) {
            case "STANDARD" -> STANDARD;
            case "PRIORITY" -> PRIORITY;
            case "SIGNAL_LOW" -> SIGNAL_LOW;
            case "FAILURE_RESTART" -> FAILURE_RESTART;
            default -> STANDARD;
        };
    }
}
