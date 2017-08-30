package com.infomaximum.cluster.core.component;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by kris on 29.12.16.
 */
public class RuntimeRoleInfo implements RemoteObject {

    public final String key;
    public final String uuid;
    public final boolean isSingleton;
    private Collection<String> classNameRControllers;

    public RuntimeRoleInfo(String key, String uuid, boolean isSingleton, Collection<Class<? extends RController>> classRControllers) {
        this(key, uuid, isSingleton);

        this.classNameRControllers=new HashSet<>();
        for (Class<? extends RController> classRController: classRControllers){
            this.classNameRControllers.add(classRController.getName());
        }
    }

    private RuntimeRoleInfo(String key, String uuid, boolean isSingleton) {
        this.key = key;
        this.uuid = uuid;
        this.isSingleton = isSingleton;
    }

    public Collection<String> getClassNameRControllers() {
        return Collections.unmodifiableCollection(classNameRControllers);
    }

    @Override
    public JSONObject serialize() {
        JSONObject out = new JSONObject();
        out.put("key", key);
        out.put("uuid", uuid);
        out.put("is_singleton", isSingleton);

        JSONArray outRControllers = new JSONArray();
        outRControllers.addAll(classNameRControllers);
        out.put("remote_controllers", outRControllers);

        return out;
    }

    public static RuntimeRoleInfo deserialize(JSONObject json) throws ClassNotFoundException {
        String key = json.getAsString("key");
        String uuid = json.getAsString("uuid");
        boolean isSingleton = (boolean)json.get("is_singleton");

        RuntimeRoleInfo subSystemInfo = new RuntimeRoleInfo(key, uuid, isSingleton);

        Collection<String> classNameRControllers = new HashSet<String>();
        for (Object oRController: (JSONArray)json.get("remote_controllers")) {
            String classNameRController = (String)oRController;
            classNameRControllers.add(classNameRController);
        }
        subSystemInfo.classNameRControllers = classNameRControllers;

        return subSystemInfo;
    }
}
