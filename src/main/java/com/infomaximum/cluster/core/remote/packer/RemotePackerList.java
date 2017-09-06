package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.core.remote.RemotePackerObjects;
import com.infomaximum.cluster.exception.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.CacheClassForName;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.*;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerList implements RemotePacker<List> {

    @Override
    public boolean isSupport(Class classType) {
        return List.class.isAssignableFrom(classType);
    }

    @Override
    public Object serialize(Component component, List value) {
        RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();

        JSONArray out = new JSONArray();
        for (Object object : value) {
            JSONObject outObject = new JSONObject();
            outObject.put("type", value.getClass());
            outObject.put("value", remotePackerObjects.serialize(object));
            out.add(outObject);
        }
        return out;
    }

    @Override
    public List deserialize(Component component, Class classType, Object value) {
        try {
            RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();

            List result = new ArrayList();
            for (Object oItem : (JSONArray) value) {
                JSONObject item = (JSONObject) oItem;
                Class iClassType = CacheClassForName.get(item.getAsString("type"));
                Object iValue = item.get("value");
                result.add(remotePackerObjects.deserialize(iClassType, iValue));
            }
            return Collections.unmodifiableList(result);
        } catch (ClassNotFoundException e) {
            throw new ClusterRemotePackerException(e);
        }
    }
}
