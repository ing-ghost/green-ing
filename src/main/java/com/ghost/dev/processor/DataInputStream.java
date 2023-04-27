package com.ghost.dev.processor;

public interface DataInputStream<T> extends Iterable<T> {
    T[] getAll();
}