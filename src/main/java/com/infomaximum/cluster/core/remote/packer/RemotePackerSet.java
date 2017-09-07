package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.core.remote.RemotePackerObjects;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import com.infomaximum.cluster.exception.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.CacheClassForName;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerSet implements RemotePacker<Set> {

    @Override
    public boolean isSupport(Class classType) {
        return Set.class.isAssignableFrom(classType);
    }

    @Override
    public String getClassName(Class classType) {
        return Set.class.getName();
    }

    @Override
    public Object serialize(Component component, Set value) {
        RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();

        JSONArray out = new JSONArray();
        for (Object oItem : value) {
            JSONObject outObject = new JSONObject();
            outObject.put("type", remotePackerObjects.getClassName(oItem.getClass()));
            outObject.put("value", remotePackerObjects.serialize(oItem));
            out.add(outObject);
        }
        return out;
    }

    @Override
    public Set deserialize(Component component, Class classType, Object value) {
        try {
            RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();

            Set result = new HashSet();
            for (Object oItem : (JSONArray) value) {
                JSONObject item = (JSONObject) oItem;
                Class iClassType = CacheClassForName.get(item.getAsString("type"));
                Object iValue = item.get("value");
                result.add(remotePackerObjects.deserialize(iClassType, iValue));
            }
            return Collections.unmodifiableSet(result);
        } catch (ClassNotFoundException e) {
            throw new ClusterRemotePackerException(e);
        }
    }
}
