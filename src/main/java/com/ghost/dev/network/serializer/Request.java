package com.ghost.dev.network.serializer;

import com.ghost.dev.processor.config.DataProcessorConfig;

public class Request<C extends DataProcessorConfig, D> {
    public final C config;
    public final D data;

    public Request(C config, D data) {
        this.config = config;
        this.data = data;
    }
}
