package com.infomaximum.cluster;

import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.RemoteControllerRequest;

import java.util.List;

public interface NetworkTransit {

    Node getNode();

    ManagerRuntimeComponent getManagerRuntimeComponent();

    RemoteControllerRequest getRemoteControllerRequest();

    List<Node> getRemoteNodes();

    void start();

    void close();

    abstract class Builder {
        public abstract NetworkTransit build(TransportManager transportManager);
    }
}
