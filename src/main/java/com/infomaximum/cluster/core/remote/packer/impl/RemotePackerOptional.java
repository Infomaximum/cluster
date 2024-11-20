package com.infomaximum.cluster.core.remote.packer.impl;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.struct.Component;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Optional;

public class RemotePackerOptional implements RemotePacker<Optional> {

    private final RemotePackerSerializable remotePackerSerializable;

    public RemotePackerOptional() {
        this.remotePackerSerializable = new RemotePackerSerializable();
    }

    @Override
    public boolean isSupport(Class classType) {
        return (classType == Optional.class);
    }

    @Override
    public String getClassName(Class classType) {
        return Optional.class.getName();
    }

    @Override
    public void validation(Type classType) {
        remotePackerSerializable.validation(classType);
    }

    @Override
    public byte[] serialize(Component component, Optional value) {
        return remotePackerSerializable.serialize(component, (Serializable) value.orElse(null));
    }

    @Override
    public Optional deserialize(Component component, Class classType, byte[] value) {
        Object result = remotePackerSerializable.deserialize(component, classType, value);
        return Optional.ofNullable(result);
    }
}
