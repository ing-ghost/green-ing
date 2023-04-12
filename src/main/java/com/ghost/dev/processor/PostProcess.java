package com.ghost.dev.processor;

import java.util.List;

public interface PostProcess<S, D> {

    D execute(S input);

    default List<D> batchProcess(List<S> input) {
        return input.stream().map(this::execute).toList();
    }

}
