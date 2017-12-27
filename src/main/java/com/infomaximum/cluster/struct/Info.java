package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.Version;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kris on 20.06.17.
 */
public class Info {

    private final String uuid;
    private final Class<? extends Component> componentClass;

    private final Class<? extends Component>[] dependencies;

    protected Info(Builder builder) {

        if (builder.uuid == null || builder.uuid.isEmpty()) throw new IllegalArgumentException("Bad uuid component");
        this.uuid = builder.uuid;

        if (builder.componentClass == null) throw new IllegalArgumentException("Not found class component");
        if (!builder.componentClass.getPackage().getName().equals(uuid)) {
            throw new IllegalArgumentException(builder.componentClass + " is not correspond to uuid: " + uuid);
        }
        this.componentClass = builder.componentClass;

        this.dependencies = builder.dependencies == null ? new Class[0] : builder.dependencies.toArray(new Class[builder.dependencies.size()]);
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

    public static class Builder {

        private final String uuid;
        private Class<? extends Component> componentClass;

        private Set<Class<? extends Component>> dependencies = new HashSet<>();

        public Builder(String uuid) {
            this.uuid = uuid;
        }

        public Builder withComponentClass(Class<? extends Component> componentClass) {
            this.componentClass = componentClass;
            return this;
        }

        public Builder withDependence(Class<? extends Component> dependence){
            dependencies.add(dependence);
            return this;
        }

        public Info build(){
            return new Info(this);
        }
    }
}
