package com.infomaximum.cluster.exception;

import java.util.List;

public class CyclicDependenceException extends ClusterException {

    public CyclicDependenceException(List<String> classNames) {
        super("Cyclic dependence in [" + String.join(", ", classNames) + "].");
    }
}
