package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.core.remote.RemotePackerObjects;
import com.infomaximum.cluster.exception.runtime.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.CacheClassForName;
import net.minidev.json.JSONObject;

import java.util.*;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerMap implements RemotePacker<Map> {

    @Override
    public boolean isSupport(Class classType) {
        return Map.class.isAssignableFrom(classType);
    }

    @Override
    public String getClassName(Class classType) {
        return Map.class.getName();
    }

    @Override
    public Object serialize(Component component, Map value) {
        RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();

        JSONObject out = new JSONObject();
        for (Object oEntry: value.entrySet()) {
            Map.Entry entry = (Map.Entry) oEntry;
            out.put((String) entry.getKey(), new JSONObject(){{
                put("type", entry.getValue().getClass().getName());
                put("value", remotePackerObjects.serialize(entry.getValue()));
            }});
        }

        return out;
    }

    @Override
    public Map deserialize(Component component, Class classType, Object value) {
        try {
            RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();

            Map out = new HashMap();
            for (Map.Entry<String, Object> entry: ((JSONObject)value).entrySet()) {
                String key = entry.getKey();

                JSONObject jEntryValue = (JSONObject) entry.getValue();
                Class iClassType = CacheClassForName.get(jEntryValue.getAsString("type"));
                Object iValue = jEntryValue.get("value");

                out.put(key, remotePackerObjects.deserialize(iClassType, iValue));
            }
            return Collections.unmodifiableMap(out);
        } catch (ClassNotFoundException e) {
            throw new ClusterRemotePackerException(e);
        }
    }
}
