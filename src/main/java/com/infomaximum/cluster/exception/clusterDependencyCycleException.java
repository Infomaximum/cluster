package com.infomaximum.cluster.exception;

import java.util.List;

public class clusterDependencyCycleException extends ClusterException {

    public clusterDependencyCycleException(List<String> classNames) {
        super("Cyclic dependence in [" + String.join(", ", classNames) + "].");
    }
}
