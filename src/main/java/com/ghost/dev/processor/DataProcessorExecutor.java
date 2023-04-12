package com.ghost.dev.processor;

import java.util.List;

public final class DataProcessorExecutor<T, E> {

    final DataProcessor<T, E> dataProcessor;

    public DataProcessorExecutor(DataProcessor<T, E> dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public E execute(DataInputStream<T> dataStream) {
        long start = System.currentTimeMillis();
        E result = dataProcessor.processData(dataStream);
        long stop = System.currentTimeMillis();

        System.out.println("Speed: " + (stop - start));

        return result;
    }

    public static <T, D1, D2> List<D2> processData(
            DataProcessor<T, List<D1>> dataProcessor,
            DataInputStream<T> inputStream,
            PostProcess<D1, D2> postProcess) {
        DataProcessorExecutor<T, List<D1>> dataProcessorExecutor = new DataProcessorExecutor<>(dataProcessor);
        List<D1> result = dataProcessorExecutor.execute(inputStream);
        return postProcess.batchProcess(result);
    }

}
