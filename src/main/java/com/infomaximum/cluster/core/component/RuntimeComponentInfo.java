package com.infomaximum.cluster.core.component;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by kris on 29.12.16.
 */
public class RuntimeComponentInfo implements RemoteObject {

    public final int id;
    public final String uuid;
    private final HashSet<String> classNameRControllers;

    public RuntimeComponentInfo(
            int id, String uuid,
            HashSet<Class<? extends RController>> classRControllers
    ) {
        this.id = id;
        this.uuid = uuid;

        this.classNameRControllers = new HashSet<>();
        for (Class<? extends RController> classRController : classRControllers) {
            this.classNameRControllers.add(classRController.getName());
        }
    }

    public RuntimeComponentInfo(
            String uuid,
            HashSet<Class<? extends RController>> classRControllers
    ) {
        this.id = -1;
        this.uuid = uuid;

        this.classNameRControllers = new HashSet<>();
        for (Class<? extends RController> classRController : classRControllers) {
            this.classNameRControllers.add(classRController.getName());
        }
    }

    public Collection<String> getClassNameRControllers() {
        return Collections.unmodifiableCollection(classNameRControllers);
    }

    public static RuntimeComponentInfo upgrade(int id, RuntimeComponentInfo source) {
        RuntimeComponentInfo result = new RuntimeComponentInfo(
                id,
                source.uuid,
                new HashSet<>()
        );
        result.classNameRControllers.addAll(source.classNameRControllers);
        return result;
    }
}
