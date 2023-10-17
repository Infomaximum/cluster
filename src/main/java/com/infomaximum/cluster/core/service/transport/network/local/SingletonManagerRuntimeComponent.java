package com.infomaximum.cluster.core.service.transport.network.local;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.LocationRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class SingletonManagerRuntimeComponent implements ManagerRuntimeComponent {

    private final UUID nodeRuntimeId;
    private final LocalManagerRuntimeComponent localManagerRuntimeComponent;

    public SingletonManagerRuntimeComponent(UUID nodeRuntimeId) {
        this.nodeRuntimeId = nodeRuntimeId;
        this.localManagerRuntimeComponent = new LocalManagerRuntimeComponent();
    }

    @Override
    public LocationRuntimeComponent find(String uuid) {
        RuntimeComponentInfo runtimeComponentInfo = localManagerRuntimeComponent.find(uuid);
        if (runtimeComponentInfo != null) {
            return new LocationRuntimeComponent(nodeRuntimeId, runtimeComponentInfo);
        } else {
            return null;
        }
    }

    @Override
    public LocationRuntimeComponent get(UUID nodeRuntimeId, int componentId) {
        if (!this.nodeRuntimeId.equals(nodeRuntimeId)) {
            return null;
        }
        RuntimeComponentInfo runtimeComponentInfo = localManagerRuntimeComponent.get(componentId);
        if (runtimeComponentInfo == null) {
            return null;
        }
        return new LocationRuntimeComponent(nodeRuntimeId, runtimeComponentInfo);
    }

    @Override
    public Collection<LocationRuntimeComponent> find(Class<? extends RController> remoteControllerClazz) {
        return localManagerRuntimeComponent.find(remoteControllerClazz).stream()
                .map(componentInfo -> new LocationRuntimeComponent(nodeRuntimeId, componentInfo))
                .collect(Collectors.toList());
    }

    @Override
    public LocalManagerRuntimeComponent getLocalManagerRuntimeComponent() {
        return localManagerRuntimeComponent;
    }
}
