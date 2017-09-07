package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerLong implements RemotePacker<Long> {

    @Override
    public boolean isSupport(Class classType) {
        return (classType == Long.class || classType == long.class);
    }

    @Override
    public Object serialize(Component component, Long value) {
        return value;
    }

    @Override
    public Long deserialize(Component component, Class classType, Object value) {
        return (Long) value;
    }
}
