package com.infomaximum.cluster.core.service.transport.network.local;

import com.infomaximum.cluster.NetworkTransit;
import com.infomaximum.cluster.Node;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.RemoteControllerRequest;

import java.util.Collections;
import java.util.List;

public class LocalNetworkTransit implements NetworkTransit {

    private final Node node;

    private final ManagerRuntimeComponent managerRuntimeComponent;

    private LocalNetworkTransit() {
        this.node = new SingletonNode();
        this.managerRuntimeComponent = new SingletonManagerRuntimeComponent(node.getRuntimeId());
    }

    @Override
    public Node getNode() {
        return node;
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
    public List<Node> getRemoteNodes() {
        return Collections.emptyList();
    }

    @Override
    public void start() {

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
