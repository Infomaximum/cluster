package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerString implements RemotePacker<String> {

    @Override
    public boolean isSupport(Class classType) {
        return (classType == String.class);
    }

    @Override
    public Object serialize(Component component, String value) {
        return value;
    }

    @Override
    public String deserialize(Component component, Class classType, Object value) {
        return (String) value;
    }
}
