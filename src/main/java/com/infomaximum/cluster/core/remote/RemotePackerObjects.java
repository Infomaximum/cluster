package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.exception.runtime.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.ReflectionUtils;

import java.lang.reflect.Type;
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

    public byte[] serialize(Object value) {
        for (RemotePacker remotePackerObject: remotePackers) {
            if (remotePackerObject.isSupport(value.getClass())) return remotePackerObject.serialize(component, value);
        }
        throw new ClusterRemotePackerException();
    }

    public Object deserialize(Class classType, byte[] value) {
        for (RemotePacker remotePackerObject: remotePackers) {
            if (remotePackerObject.isSupport(classType)) return remotePackerObject.deserialize(component, classType, value);
        }
        throw new ClusterRemotePackerException();
    }

    public String getClassName(Class classType){
        for (RemotePacker remotePackerObject: remotePackers) {
            if (remotePackerObject.isSupport(classType)) return remotePackerObject.getClassName(classType);
        }
        throw new ClusterRemotePackerException();
    }

    public boolean isSupportAndValidationType(Type classType) {
        for (RemotePacker remotePackerObject: remotePackers) {
            if (remotePackerObject.isSupport(ReflectionUtils.getRawClass(classType))) {
                //Валидируем класс
                remotePackerObject.validation(classType);
                return true;
            }
        }
        return false;
    }
}
