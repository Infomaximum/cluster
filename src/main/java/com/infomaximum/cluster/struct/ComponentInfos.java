package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.core.component.RuntimeRoleInfo;
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

    private Collection<RuntimeRoleInfo> items;

    public ComponentInfos(Collection<RuntimeRoleInfo> items) {
        this.items = items;
    }

    public Collection<RuntimeRoleInfo> getItems() {
        return items;
    }

    @Override
    public JSONObject serialize() {
        JSONObject out = new JSONObject();

        JSONArray jItems = new JSONArray();
        for (RuntimeRoleInfo subSystemInfo: items) {
            jItems.add(subSystemInfo.serialize());
        }
        out.put("items", jItems);

        return out;
    }

    public static ComponentInfos deserialize(JSONObject json) throws ClassNotFoundException {
        List<RuntimeRoleInfo> items = new ArrayList<RuntimeRoleInfo>();
        for (Object oItem: (JSONArray)json.get("items")) {
            items.add(RuntimeRoleInfo.deserialize((JSONObject) oItem));
        }
        return new ComponentInfos(items);
    }
}
