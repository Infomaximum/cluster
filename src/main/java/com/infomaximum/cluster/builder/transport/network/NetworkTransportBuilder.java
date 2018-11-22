package com.infomaximum.cluster.builder.transport.network;

import com.infomaximum.cluster.core.remote.packer.*;
import com.infomaximum.cluster.core.service.transport.network.NetworkTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 15.06.17.
 */
public abstract class NetworkTransportBuilder {

    private List<RemotePacker> remotePackers;

    public NetworkTransportBuilder() {
        this.remotePackers = new ArrayList<>();
        this.remotePackers.add(new RemotePackerRemoteObject());
        this.remotePackers.add(new RemotePackerSerializable());
        this.remotePackers.add(new RemotePackerFuture());
        this.remotePackers.add(new RemotePackerClasterInputStream());
    }

    public NetworkTransportBuilder withRemotePackerObject(RemotePacker remotePackerObject) {
        remotePackers.add(remotePackerObject);
        return this;
    }

    protected List<RemotePacker> getRemotePackers() {
        return remotePackers;
    }

    public abstract NetworkTransport build();
}
