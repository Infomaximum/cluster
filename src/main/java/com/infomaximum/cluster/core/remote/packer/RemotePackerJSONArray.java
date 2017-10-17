package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;
import net.minidev.json.JSONArray;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerJSONArray implements RemotePacker<JSONArray> {

    @Override
    public boolean isSupport(Class classType) {
        return JSONArray.class.isAssignableFrom(classType);
    }

    @Override
    public String getClassName(Class classType) {
        return JSONArray.class.getName();
    }

    @Override
    public Object serialize(Component component, JSONArray value) {
        return value;
    }

    @Override
    public JSONArray deserialize(Component component, Class classType, Object value) {
        return (JSONArray) value;
    }
}
