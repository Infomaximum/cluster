package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerInteger implements RemotePacker<Integer> {

    @Override
    public boolean isSupport(Class classType) {
        return (classType == Integer.class || classType == int.class);
    }

    @Override
    public Object serialize(Component component, Integer value) {
        return value;
    }

    @Override
    public Integer deserialize(Component component, Class classType, Object value) {
        return (Integer) value;
    }
}
