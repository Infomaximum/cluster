package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.exception.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;

import java.util.List;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerObjects {

    private final Component component;
    private final List<RemotePacker> remotePackers;

    public RemotePackerObjects(Remotes remotes) {
        this.component = remotes.component;
        this.remotePackers = component.getTransport().getRemotePackers();
    }

    public Object serialize(Object value) {
        for (RemotePacker remotePackerObject: remotePackers) {
            if (remotePackerObject.isSupport(value.getClass())) return remotePackerObject.serialize(component, value);
        }
        throw new ClusterRemotePackerException();
    }

    public Object deserialize(Class classType, Object value){
        for (RemotePacker remotePackerObject: remotePackers) {
            if (remotePackerObject.isSupport(classType)) return remotePackerObject.deserialize(component, classType, value);
        }
        throw new ClusterRemotePackerException();
    }

}
