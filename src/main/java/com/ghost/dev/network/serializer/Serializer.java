package com.ghost.dev.network.serializer;

import java.io.IOException;

public interface Serializer<E> {

    String serialize(E result) throws IOException;

}
