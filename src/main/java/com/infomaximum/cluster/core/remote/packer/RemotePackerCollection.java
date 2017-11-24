package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.core.remote.RemotePackerObjects;
import com.infomaximum.cluster.exception.runtime.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerCollection implements RemotePacker<Collection> {

    @Override
    public boolean isSupport(Class classType) {
        return Collection.class.isAssignableFrom(classType);
    }

    @Override
    public String getClassName(Class classType) {
        return classType.getName();
    }

    @Override
    public Object serialize(Component component, Collection value) {
        RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();

        JSONObject out = new JSONObject();
        out.put("type", getClassName(value.getClass()));

        JSONArray items = new JSONArray();
        for (Object oItem : value) {
            JSONObject outItemObject = new JSONObject();
            outItemObject.put("type", remotePackerObjects.getClassName(oItem.getClass()));
            outItemObject.put("value", remotePackerObjects.serialize(oItem));
            items.add(outItemObject);
        }
        out.put("items", items);

        return out;
    }

    @Override
    public Collection deserialize(Component component, Class classType, Object value) {
        try {
            RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();

            JSONObject jValue = (JSONObject) value;

            Class collectionClassType = Class.forName(jValue.getAsString("type"), true, Thread.currentThread().getContextClassLoader());
            Collection result = (Collection) collectionClassType.newInstance();

            for (Object oItem : (JSONArray) jValue.get("items")) {
                JSONObject item = (JSONObject) oItem;
                Class iClassType = Class.forName(item.getAsString("type"), true, Thread.currentThread().getContextClassLoader());
                Object iValue = item.get("value");
                result.add(remotePackerObjects.deserialize(iClassType, iValue));
            }
            return result;
        } catch (ReflectiveOperationException e) {
            throw new ClusterRemotePackerException(e);
        }
    }
}
