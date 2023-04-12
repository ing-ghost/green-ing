package com.ghost.dev.network.serializer;

import com.ghost.dev.processor.config.DataProcessorConfig;

import java.io.IOException;
import java.io.InputStream;

public interface Deserialize<C extends DataProcessorConfig, D> {

    Request<C, D> deserialize(InputStream inputStream) throws IOException;

}
