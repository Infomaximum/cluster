package com.infomaximum.cluster.core.service.transport.network;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.local.LocalManagerRuntimeComponent;

import java.util.Collection;
import java.util.UUID;

public interface ManagerRuntimeComponent {

    LocationRuntimeComponent find(String uuid);

    LocationRuntimeComponent get(UUID nodeRuntimeId, int componentId);

    Collection<LocationRuntimeComponent> find(Class<? extends RController> remoteControllerClazz);

    LocalManagerRuntimeComponent getLocalManagerRuntimeComponent();

}
