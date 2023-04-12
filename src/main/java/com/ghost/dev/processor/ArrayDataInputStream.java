package com.ghost.dev.processor;

import java.util.Iterator;

public final class ArrayDataInputStream<T> implements DataInputStream<T> {

    private final T[] data;

    public ArrayDataInputStream(T[] data) {
        this.data = data;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < data.length;
            }

            @Override
            public T next() {
                return data[index++];
            }
        };
    }
}

