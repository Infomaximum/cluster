package com.infomaximum.cluster.core.component;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import com.infomaximum.cluster.struct.Version;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by kris on 29.12.16.
 */
public class RuntimeComponentInfo implements RemoteObject {

    public final int id;
    public final String uuid;
    public final Version version;
    private final HashSet<String> classNameRControllers;

    public RuntimeComponentInfo(
            int id, String uuid, Version version,
            HashSet<Class<? extends RController>> classRControllers
    ) {
        this.id = id;
        this.uuid = uuid;
        this.version = version;

        this.classNameRControllers = new HashSet<>();
        for (Class<? extends RController> classRController : classRControllers) {
            this.classNameRControllers.add(classRController.getName());
        }
    }

    public RuntimeComponentInfo(
            String uuid,
            Version version,
            HashSet<Class<? extends RController>> classRControllers
    ) {
        this(-1, uuid, version, classRControllers);
    }

    public Collection<String> getClassNameRControllers() {
        return Collections.unmodifiableCollection(classNameRControllers);
    }

    public static RuntimeComponentInfo upgrade(int id, RuntimeComponentInfo source) {
        RuntimeComponentInfo result = new RuntimeComponentInfo(
                id,
                source.uuid,
                source.version,
                new HashSet<>()
        );
        result.classNameRControllers.addAll(source.classNameRControllers);
        return result;
    }
}
