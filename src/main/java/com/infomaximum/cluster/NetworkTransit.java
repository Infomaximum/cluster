package com.infomaximum.cluster;

import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.RemoteControllerRequest;

public interface NetworkTransit {

    byte getNode();

    ManagerRuntimeComponent getManagerRuntimeComponent();

    RemoteControllerRequest getRemoteControllerRequest();

    void close();

    abstract class Builder {
        public abstract NetworkTransit build(TransportManager transportManager);
    }
}
