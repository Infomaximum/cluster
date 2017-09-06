package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerJSONObject implements RemotePacker<JSONObject> {

    @Override
    public boolean isSupport(Class classType) {
        return JSONObject.class.isAssignableFrom(classType);
    }

    @Override
    public String getClassName(Class classType) {
        return JSONObject.class.getName();
    }

    @Override
    public Object serialize(Component component, JSONObject value) {
        return value;
    }

    @Override
    public JSONObject deserialize(Component component, Class classType, Object value) {
        return (JSONObject) value;
    }
}
