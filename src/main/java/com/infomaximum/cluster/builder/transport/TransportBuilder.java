package com.infomaximum.cluster.builder.transport;

import com.infomaximum.cluster.core.remote.packer.*;
import com.infomaximum.cluster.core.service.transport.TransportManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 15.06.17.
 */
public abstract class TransportBuilder {

    private List<RemotePacker> remotePackers;

    public TransportBuilder() {
        this.remotePackers=new ArrayList<>();
        this.remotePackers.add(new RemotePackerString());
        this.remotePackers.add(new RemotePackerBoolean());
        this.remotePackers.add(new RemotePackerLong());
        this.remotePackers.add(new RemotePackerInteger());
        this.remotePackers.add(new RemotePackerByte());
        this.remotePackers.add(new RemotePackerJSONObject());
        this.remotePackers.add(new RemotePackerJSONArray());
        this.remotePackers.add(new RemotePackerRemoteObject());
        this.remotePackers.add(new RemotePackerMap());
        this.remotePackers.add(new RemotePackerSet());
        this.remotePackers.add(new RemotePackerList());
        this.remotePackers.add(new RemotePackerSerializable());
        this.remotePackers.add(new RemotePackerFuture());
    }

    public TransportBuilder withRemotePackerObject(RemotePacker remotePackerObject) {
        remotePackers.add(remotePackerObject);
        return this;
    }

    protected List<RemotePacker> getRemotePackers() {
        return remotePackers;
    }

    public abstract TransportManager build();
}
