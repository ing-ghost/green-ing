package com.ghost.dev.network.serializer;

import java.io.IOException;

public interface Serializer<E> {

    byte[] serialize(E result) throws IOException;

}
