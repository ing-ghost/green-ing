package com.ghost.dev.atm.model;

public final class AtmStatus {
    public static final int STANDARD = 3;
    public static final int PRIORITY = 1;
    public static final int SIGNAL_LOW = 2;
    public static final int FAILURE_RESTART = 0;

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
