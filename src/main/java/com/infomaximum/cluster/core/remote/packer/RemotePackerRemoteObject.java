package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import com.infomaximum.cluster.exception.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;
import net.minidev.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerRemoteObject implements RemotePacker<RemoteObject> {

    @Override
    public boolean isSupport(Class classType) {
        return RemoteObject.instanceOf(classType);
    }

    @Override
    public Object serialize(Component component, RemoteObject value) {
        return value.serialize(component);
    }

    @Override
    public RemoteObject deserialize(Component component, Class classType, Object value) {
        try {
            return RemoteObject.deserialize(component, classType, (JSONObject) value);
        } catch (ReflectiveOperationException e) {
            throw new ClusterRemotePackerException(e);
        }
    }
}
