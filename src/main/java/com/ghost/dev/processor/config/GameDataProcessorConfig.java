package com.ghost.dev.processor.config;

public final class GameDataProcessorConfig implements DataProcessorConfig {
    public final int groupCount;

    public GameDataProcessorConfig(int groupCount) {
        this.groupCount = groupCount;
    }
}
