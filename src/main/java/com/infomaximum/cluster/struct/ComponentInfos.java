package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by kris on 29.12.16.
 */
public class ComponentInfos implements RemoteObject {

    private Collection<RuntimeComponentInfo> items;

    public ComponentInfos(Collection<RuntimeComponentInfo> items) {
        this.items = items;
    }

    public Collection<RuntimeComponentInfo> getItems() {
        return items;
    }

    @Override
    public JSONObject serialize(Component component) {
        JSONObject out = new JSONObject();

        JSONArray jItems = new JSONArray();
        for (RuntimeComponentInfo subSystemInfo: items) {
            jItems.add(subSystemInfo.serialize(component));
        }
        out.put("items", jItems);

        return out;
    }

    public static ComponentInfos deserialize(JSONObject json) throws ClassNotFoundException {
        List<RuntimeComponentInfo> items = new ArrayList<RuntimeComponentInfo>();
        for (Object oItem: (JSONArray)json.get("items")) {
            items.add(RuntimeComponentInfo.deserialize((JSONObject) oItem));
        }
        return new ComponentInfos(items);
    }
}
