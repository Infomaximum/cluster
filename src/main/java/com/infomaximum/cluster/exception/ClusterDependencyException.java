package com.infomaximum.cluster.exception;

import com.infomaximum.cluster.struct.Component;

public class ClusterDependencyException extends ClusterException {

    public ClusterDependencyException(Component referencingComponent, Component removingComponent) {
        super(String.format("Component %s referenced to removing %s.",
                referencingComponent.getClass().getName(),
                removingComponent.getClass().getName()));
    }
}
