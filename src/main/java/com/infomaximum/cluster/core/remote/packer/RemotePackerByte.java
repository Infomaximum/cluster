package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerByte implements RemotePacker<Byte> {

    @Override
    public boolean isSupport(Class classType) {
        return (classType == Byte.class || classType == byte.class);
    }

    @Override
    public Object serialize(Component component, Byte value) {
        return value;
    }

    @Override
    public Byte deserialize(Component component, Class classType, Object value) {
        return (Byte) value;
    }
}
