package com.infomaximum.cluster.networktransit;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.core.service.transport.executor.ComponentExecutorTransport;
import com.infomaximum.cluster.core.service.transport.network.RemoteControllerRequest;
import com.infomaximum.cluster.struct.Component;

import java.util.UUID;


public class FakeRemoteControllerRequest implements RemoteControllerRequest {

    private final FakeNetworkTransit fakeNetworkTransit;

    public FakeRemoteControllerRequest(FakeNetworkTransit fakeNetworkTransit) {
        this.fakeNetworkTransit = fakeNetworkTransit;
    }

    @Override
    public ComponentExecutorTransport.Result request(Component sourceComponent, UUID targetNodeRuntimeId, int targetComponentId, String rControllerClassName, int methodKey, byte[][] args) throws Exception {
        Cluster cluster = fakeNetworkTransit.spaceNetworkTransit.getCluster(targetNodeRuntimeId);
        return cluster.getTransportManager().localRequest(
                targetComponentId,
                rControllerClassName,
                methodKey,
                args
        );
    }
}
