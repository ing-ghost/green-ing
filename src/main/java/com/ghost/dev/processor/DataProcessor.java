package com.ghost.dev.processor;


public interface DataProcessor<T, E> {

    E processData(DataInputStream<T> dataStream);

}
