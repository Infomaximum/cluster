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

    public final int uniqueId;
    public final Info info;
    public final boolean isSingleton;
    private final HashSet<String> classNameRControllers;

    public RuntimeComponentInfo(int uniqueId, Info info, boolean isSingleton, HashSet<Class<? extends RController>> classRControllers) {
        this.uniqueId = uniqueId;
        this.info = info;
        this.isSingleton = isSingleton;

        this.classNameRControllers = new HashSet<>();
        for (Class<? extends RController> classRController : classRControllers) {
            this.classNameRControllers.add(classRController.getName());
        }
    }

    public RuntimeComponentInfo(Info info, boolean isSingleton, HashSet<Class<? extends RController>> classRControllers) {
        this.uniqueId = -1;
        this.info = info;
        this.isSingleton = isSingleton;

        this.classNameRControllers = new HashSet<>();
        for (Class<? extends RController> classRController : classRControllers) {
            this.classNameRControllers.add(classRController.getName());
        }
    }

    public Collection<String> getClassNameRControllers() {
        return Collections.unmodifiableCollection(classNameRControllers);
    }

    public static RuntimeComponentInfo upgrade(int uniqueId, RuntimeComponentInfo source) {
        RuntimeComponentInfo result = new RuntimeComponentInfo(
                uniqueId,
                source.info,
                source.isSingleton,
                new HashSet<>()
        );
        result.classNameRControllers.addAll(source.classNameRControllers);
        return result;
    }
}
