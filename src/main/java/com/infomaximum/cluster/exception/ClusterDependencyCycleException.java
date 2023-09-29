package com.infomaximum.cluster.exception;

import java.util.List;

public class ClusterDependencyCycleException extends ClusterException {

    public ClusterDependencyCycleException(List<String> classNames) {
        super("Cyclic dependence in [" + String.join(", ", classNames) + "].");
    }
}
