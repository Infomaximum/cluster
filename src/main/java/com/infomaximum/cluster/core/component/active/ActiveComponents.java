package com.infomaximum.cluster.core.component.active;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;

import java.util.Collection;

/**
 * Created by kris on 14.11.16.
 */
public interface ActiveComponents {

    public Collection<RuntimeComponentInfo> registerActiveRole(RuntimeComponentInfo subSystemInfo);

    public Collection<RuntimeComponentInfo> unRegisterActiveRole(String key);

    public Collection<RuntimeComponentInfo> getActiveSubSystems();

    public Collection<String> getActiveSubSystemKeys();

    public Collection<String> getActiveSubSystemUuids();

    public boolean isActiveSubSystem(String uuid);


}
