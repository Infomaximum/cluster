package com.infomaximum.cluster.core.component.active;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;

import java.util.Collection;

/**
 * Created by kris on 14.11.16.
 */
public interface ActiveComponents {

    Collection<RuntimeComponentInfo> registerActiveRole(RuntimeComponentInfo subSystemInfo);

    Collection<RuntimeComponentInfo> unRegisterActiveRole(String key);

    Collection<RuntimeComponentInfo> getActiveSubSystems();

    Collection<String> getActiveSubSystemKeys();

    Collection<String> getActiveSubSystemUuids();

    boolean isActiveSubSystem(String uuid);
}
