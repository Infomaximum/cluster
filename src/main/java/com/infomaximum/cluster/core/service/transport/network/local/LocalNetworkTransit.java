package com.infomaximum.cluster.core.service.transport.network.local;

import com.infomaximum.cluster.NetworkTransit;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.RemoteControllerRequest;

public class LocalNetworkTransit implements NetworkTransit {

    private final ManagerRuntimeComponent managerRuntimeComponent;

    private LocalNetworkTransit() {
        this.managerRuntimeComponent = new LocalManagerRuntimeComponent();
    }

    @Override
    public byte getNode() {
        return 0;
    }

    @Override
    public ManagerRuntimeComponent getManagerRuntimeComponent() {
        return managerRuntimeComponent;
    }

    @Override
    public RemoteControllerRequest getRemoteControllerRequest() {
        return null;
    }

    @Override
    public void close() {
    }

    public static class Builder extends NetworkTransit.Builder {

        @Override
        public LocalNetworkTransit build(TransportManager transportManager) {
            return new LocalNetworkTransit();
        }

    }
}
