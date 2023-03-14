package com.infomaximum.cluster.core.remote.packer.impl;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.Type;

/**
 * Created by user on 06.09.2017.
 * TODO Ulitin V. Когда будем разъезжаться по серверам реализовать
 */
public class RemotePackerClusterInputStream implements RemotePacker<ClusterInputStream> {

    @Override
    public boolean isSupport(Class classType) {
        return ClusterInputStream.class == classType;
    }

    @Override
    public String getClassName(Class classType) {
        return ClusterInputStream.class.getName();
    }

    @Override
    public void validation(Type classType) {
    }

    @Override
    public byte[] serialize(Component component, ClusterInputStream value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ClusterInputStream deserialize(Component component, Class classType, byte[] value) {
        throw new RuntimeException("Not implemented");
    }
}
