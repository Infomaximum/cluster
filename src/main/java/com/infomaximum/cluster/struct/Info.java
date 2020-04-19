package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kris on 20.06.17.
 */
public class Info implements RemoteObject {

    private final String uuid;
    private final Class componentClass;

    private final Class[] dependencies;

    protected Info(Builder builder) {

        if (builder.uuid == null || builder.uuid.isEmpty()) throw new IllegalArgumentException("Bad uuid component");
        this.uuid = builder.uuid;

        if (builder.componentClass == null) throw new IllegalArgumentException("Not found class component");
        if (!builder.componentClass.getPackage().getName().equals(uuid)) {
            throw new IllegalArgumentException(builder.componentClass + " is not correspond to uuid: " + uuid);
        }
        this.componentClass = builder.componentClass;

        this.dependencies = builder.dependencies == null
                ? new Class[0]
                : (Class<Component>[]) builder.dependencies.toArray(new Class[builder.dependencies.size()]);
    }

    public String getUuid() {
        return uuid;
    }

    public Class<? extends Component> getComponent() {
        return componentClass;
    }

    public Class<? extends Component>[] getDependencies() {
        return dependencies;
    }

    public static class Builder<T extends Builder> {

        public final String uuid;
        private Class<? extends Component> componentClass;

        private Set<Class<? extends Component>> dependencies = new HashSet<>();

        public Builder(String uuid) {
            this.uuid = uuid;
        }

        public T withComponentClass(Class<? extends Component> componentClass) {
            this.componentClass = componentClass;
            return (T) this;
        }

        public T withDependence(Class<? extends Component> dependence) {
            dependencies.add(dependence);
            return (T) this;
        }

        public Info build(){
            return new Info(this);
        }
    }
}
