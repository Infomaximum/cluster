package com.infomaximum.cluster.networktransit;

import com.infomaximum.cluster.NetworkTransit;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.RemoteControllerRequest;
import com.infomaximum.cluster.core.service.transport.network.local.LocalManagerRuntimeComponent;

public class FakeNetworkTransit implements NetworkTransit {

    public final TransportManager transportManager;

    public final SpaceNetworkTransit spaceNetworkTransit;

    private final byte node;

    private final ManagerRuntimeComponent managerRuntimeComponent;

    private final FakeRemoteControllerRequest fakeRemoteControllerRequest;

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public FakeNetworkTransit(Builder builder, TransportManager transportManager) {
        this.transportManager = transportManager;
        this.spaceNetworkTransit = builder.spaceNetworkTransit;
        this.spaceNetworkTransit.registry(builder.node, transportManager.cluster);
        this.node = builder.node;
        this.managerRuntimeComponent = new LocalManagerRuntimeComponent();
        this.fakeRemoteControllerRequest = new FakeRemoteControllerRequest(this);
        this.uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
    }

    @Override
    public byte getNode() {
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
    public void close() {

    }

    public static class Builder extends NetworkTransit.Builder {

        private final SpaceNetworkTransit spaceNetworkTransit;

        private final byte node;
        private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        public Builder(SpaceNetworkTransit spaceNetworkTransit, byte node, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.spaceNetworkTransit = spaceNetworkTransit;
            this.node = node;
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        }


        public FakeNetworkTransit build(TransportManager transportManager) {
            return new FakeNetworkTransit(this, transportManager);
        }

    }

}
