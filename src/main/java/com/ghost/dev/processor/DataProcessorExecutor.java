package com.ghost.dev.processor;

import com.ghost.dev.processor.config.DataProcessorConfig;

import java.util.List;

public final class DataProcessorExecutor<C extends DataProcessorConfig, T, E> {

    final DataProcessor<C, T, E> dataProcessor;

    public DataProcessorExecutor(DataProcessor<C, T, E> dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public E execute(C config, DataInputStream<T> dataStream) {
        return dataProcessor.processData(config, dataStream);
    }

    public static <C extends DataProcessorConfig, T, D1, D2> List<D2> processData(
            DataProcessor<C, T, List<D1>> dataProcessor,
            C config,
            DataInputStream<T> inputStream,
            PostProcess<D1, D2> postProcess) {
        DataProcessorExecutor<C, T, List<D1>> dataProcessorExecutor = new DataProcessorExecutor<>(dataProcessor);
        List<D1> result = dataProcessorExecutor.execute(config, inputStream);
        return postProcess.batchProcess(result);
    }

}
