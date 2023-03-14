package com.infomaximum.cluster.core.remote.packer.impl;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import com.infomaximum.cluster.core.remote.utils.validatorremoteobject.RemoteObjectValidator;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.Type;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerRemoteObject implements RemotePacker<RemoteObject> {

    private final RemotePackerSerializable remotePackerSerializable;

    public RemotePackerRemoteObject() {
        this.remotePackerSerializable = new RemotePackerSerializable();
    }

    @Override
    public boolean isSupport(Class classType) {
        return RemoteObject.class.isAssignableFrom(classType);
    }

    @Override
    public void validation(Type classType) {
        RemoteObjectValidator.validation(classType).check();
    }

    @Override
    public byte[] serialize(Component component, RemoteObject value) {
        return remotePackerSerializable.serialize(component, value);
    }

    @Override
    public RemoteObject deserialize(Component component, Class classType, byte[] value) {
        return (RemoteObject) remotePackerSerializable.deserialize(component, classType, value);
    }
}
