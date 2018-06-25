package com.infomaximum.cluster.core.component;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import com.infomaximum.cluster.struct.Info;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by kris on 29.12.16.
 */
public class RuntimeComponentInfo implements RemoteObject {

    public final String key;
    public final Info info;
    public final boolean isSingleton;
    private HashSet<String> classNameRControllers;

    public RuntimeComponentInfo(String key, Info info, boolean isSingleton, HashSet<Class<? extends RController>> classRControllers) {
        this.key = key;
        this.info = info;
        this.isSingleton = isSingleton;

        this.classNameRControllers=new HashSet<>();
        for (Class<? extends RController> classRController: classRControllers){
            this.classNameRControllers.add(classRController.getName());
        }
    }

    public Collection<String> getClassNameRControllers() {
        return Collections.unmodifiableCollection(classNameRControllers);
    }
}
