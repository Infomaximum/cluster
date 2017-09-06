package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerBoolean implements RemotePacker<Boolean> {

    @Override
    public boolean isSupport(Class classType) {
        return (classType == Boolean.class || classType == boolean.class);
    }

    @Override
    public Object serialize(Component component, Boolean value) {
        return value;
    }

    @Override
    public Boolean deserialize(Component component, Class classType, Object value) {
        return (Boolean) value;
    }
}
