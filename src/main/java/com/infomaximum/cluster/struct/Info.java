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

    private final Class<? extends Component> componentClass;
    private final String uuid;
    private final Class<? extends Component>[] dependencies;

    protected Info(Builder builder) {
        this.componentClass = builder.componentClass;
        this.uuid = builder.uuid;
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

        private String uuid;
        private final Class<? extends Component> componentClass;

        private Set<Class<? extends Component>> dependencies = new HashSet<>();

        public Builder(String uuid, Class<? extends Component> componentClass) {
            this.componentClass = componentClass;
            this.uuid = uuid;
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
