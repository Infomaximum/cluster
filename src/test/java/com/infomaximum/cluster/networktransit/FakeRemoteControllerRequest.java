package com.infomaximum.cluster.networktransit;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.core.service.transport.executor.ComponentExecutorTransport;
import com.infomaximum.cluster.core.service.transport.network.RemoteControllerRequest;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.GlobalUniqueIdUtils;


public class FakeRemoteControllerRequest implements RemoteControllerRequest {

    private final FakeNetworkTransit fakeNetworkTransit;

    public FakeRemoteControllerRequest(FakeNetworkTransit fakeNetworkTransit) {
        this.fakeNetworkTransit = fakeNetworkTransit;
    }

    @Override
    public ComponentExecutorTransport.Result request(Component sourceComponent, int targetComponentUniqueId, String rControllerClassName, int methodKey, byte[][] args) throws Exception {
        byte node = GlobalUniqueIdUtils.getNode(targetComponentUniqueId);
        Cluster cluster = fakeNetworkTransit.spaceNetworkTransit.getCluster(node);
        return cluster.getTransportManager().localRequest(
                targetComponentUniqueId,
                rControllerClassName,
                methodKey,
                args
        );
    }
}
