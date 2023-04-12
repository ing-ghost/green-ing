package com.ghost.dev.processor;


import com.ghost.dev.processor.config.DataProcessorConfig;

public interface DataProcessor<C extends DataProcessorConfig, T, E> {

    E processData(C config, DataInputStream<T> dataStream);

}
