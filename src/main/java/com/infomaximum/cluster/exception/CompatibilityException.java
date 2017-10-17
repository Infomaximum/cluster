package com.infomaximum.cluster.exception;

import com.infomaximum.cluster.Version;
import com.infomaximum.cluster.struct.Component;

public class CompatibilityException extends ClusterException {

    public CompatibilityException(Component component, Version environmentVersion) {
        super(component.getClass().getName() + " (ver. " + component.getInfo().getEnvironmentVersion() + ") is incompatible with the environment ver. " + environmentVersion.toString());
    }
}
