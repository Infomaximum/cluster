package com.infomaximum.cluster.networktransit;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.NetworkTransit;
import com.infomaximum.cluster.Node;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.RemoteControllerRequest;

import java.util.ArrayList;
import java.util.List;

public class FakeNetworkTransit implements NetworkTransit {

    public final TransportManager transportManager;

    public final SpaceNetworkTransit spaceNetworkTransit;

    private final Node node;

    private final ManagerRuntimeComponent managerRuntimeComponent;

    private final FakeRemoteControllerRequest fakeRemoteControllerRequest;

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public FakeNetworkTransit(Builder builder, TransportManager transportManager) {
        this.transportManager = transportManager;
        this.spaceNetworkTransit = builder.spaceNetworkTransit;
        this.spaceNetworkTransit.registry(builder.node.getRuntimeId(), transportManager.cluster);
        this.node = builder.node;
        this.managerRuntimeComponent = new FakeManagerRuntimeComponent(builder.spaceNetworkTransit, builder.node.getRuntimeId());
        this.fakeRemoteControllerRequest = new FakeRemoteControllerRequest(this);
        this.uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
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
        return fakeRemoteControllerRequest;
    }

    @Override
    public List<Node> getRemoteNodes() {
        List<Node> nodes = new ArrayList<>();
        for (Cluster cluster : spaceNetworkTransit.getClusters()) {
            if (cluster.node.equals(getNode())) {
                continue;
            }
            nodes.add(cluster.node);
        }
        return nodes;
    }

    @Override
    public void start() {

    }

    @Override
    public void close() {

    }

    public static class Builder extends NetworkTransit.Builder {

        private final SpaceNetworkTransit spaceNetworkTransit;

        private final Node node;
        private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        public Builder(SpaceNetworkTransit spaceNetworkTransit, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.spaceNetworkTransit = spaceNetworkTransit;
            this.node = new FakeNode();
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        }


        public FakeNetworkTransit build(TransportManager transportManager) {
            return new FakeNetworkTransit(this, transportManager);
        }

    }

}
