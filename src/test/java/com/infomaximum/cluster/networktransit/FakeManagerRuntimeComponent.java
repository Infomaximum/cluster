package com.infomaximum.cluster.networktransit;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.LocationRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.local.LocalManagerRuntimeComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class FakeManagerRuntimeComponent implements ManagerRuntimeComponent {

    private final SpaceNetworkTransit spaceNetworkTransit;

    private final UUID nodeRuntimeId;
    private final LocalManagerRuntimeComponent localManagerRuntimeComponent;

    public FakeManagerRuntimeComponent(SpaceNetworkTransit spaceNetworkTransit, UUID nodeRuntimeId) {
        this.spaceNetworkTransit = spaceNetworkTransit;
        this.nodeRuntimeId = nodeRuntimeId;
        this.localManagerRuntimeComponent = new LocalManagerRuntimeComponent();
    }

    @Override
    public LocationRuntimeComponent find(String uuid) {
        RuntimeComponentInfo runtimeComponentInfo = localManagerRuntimeComponent.find(uuid);
        if (runtimeComponentInfo != null) {
            return new LocationRuntimeComponent(nodeRuntimeId, runtimeComponentInfo);
        }

        for (Cluster cluster : spaceNetworkTransit.getClusters()) {
            if (cluster.node.getRuntimeId() == nodeRuntimeId) {
                continue;
            }
            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            runtimeComponentInfo = managerComponent.getRegisterComponent().getLocalComponent(uuid);
            if (runtimeComponentInfo != null) {
                return new LocationRuntimeComponent(cluster.node.getRuntimeId(), runtimeComponentInfo);
            }
        }
        return null;
    }

    @Override
    public LocationRuntimeComponent get(UUID nodeRuntimeId, int componentId) {
        Cluster cluster = spaceNetworkTransit.getCluster(nodeRuntimeId);
        RuntimeComponentInfo runtimeComponentInfo = cluster.getTransportManager().networkTransit.getManagerRuntimeComponent().getLocalManagerRuntimeComponent().get(componentId);
        if (runtimeComponentInfo == null) {
            return null;
        }
        return new LocationRuntimeComponent(nodeRuntimeId, runtimeComponentInfo);
    }

    @Override
    public Collection<LocationRuntimeComponent> find(Class<? extends RController> remoteControllerClazz) {
        List<LocationRuntimeComponent> components = new ArrayList<>();
        for (Cluster cluster : spaceNetworkTransit.getClusters()) {
            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            for (RuntimeComponentInfo componentInfo : managerComponent.getRegisterComponent().findLocalComponent(remoteControllerClazz)) {
                components.add(new LocationRuntimeComponent(cluster.node.getRuntimeId(), componentInfo));
            }
        }
        return components;
    }

    @Override
    public LocalManagerRuntimeComponent getLocalManagerRuntimeComponent() {
        return localManagerRuntimeComponent;
    }
}
