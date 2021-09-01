package com.infomaximum.cluster.core.service.transport.network;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;

import java.util.Collection;

public interface ManagerRuntimeComponent {

    void registerComponent(RuntimeComponentInfo subSystemInfo);

    boolean unRegisterComponent(int uniqueId);

    Collection<RuntimeComponentInfo> getComponents();

    RuntimeComponentInfo find(String uuid, Class<? extends RController> remoteControllerClazz);

    Collection<RuntimeComponentInfo> find(Class<? extends RController> remoteControllerClazz);
}
