package com.infomaximum.cluster.core.component.environment;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;

import java.util.Collection;

public interface EnvironmentComponents {

    Collection<RuntimeComponentInfo> getActiveComponents();

}
