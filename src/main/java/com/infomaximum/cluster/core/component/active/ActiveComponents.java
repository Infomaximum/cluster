package com.infomaximum.cluster.core.component.active;

import com.infomaximum.cluster.core.component.RuntimeRoleInfo;

import java.util.Collection;

/**
 * Created by kris on 14.11.16.
 */
public interface ActiveComponents {

    public Collection<RuntimeRoleInfo> registerActiveRole(RuntimeRoleInfo subSystemInfo);

    public Collection<RuntimeRoleInfo> unRegisterActiveRole(String key);

    public Collection<RuntimeRoleInfo> getActiveSubSystems();

    public Collection<String> getActiveSubSystemKeys();

    public Collection<String> getActiveSubSystemUuids();

    public boolean isActiveSubSystem(String uuid);


}
