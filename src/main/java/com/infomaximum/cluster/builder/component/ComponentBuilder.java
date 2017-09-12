package com.infomaximum.cluster.builder.component;

import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.struct.config.ComponentConfig;

/**
 * Created by kris on 19.06.17.
 */
public class ComponentBuilder {

    public final Class<? extends Component> classRole;
    public final Info info;

    private ComponentConfig config;

    public ComponentBuilder(Class<? extends Component> classRole) throws ReflectiveOperationException {
        this.classRole = classRole;
        info = (Info) classRole.getField("INFO").get(null);
    }

    public ComponentBuilder withConfig(ComponentConfig config) {
        this.config = config;
        return this;
    }

    public Class<? extends Component> getClassRole() {
        return classRole;
    }
    public ComponentConfig getConfig() {
        return config;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentBuilder that = (ComponentBuilder) o;
        return classRole.equals(that.classRole);
    }

    @Override
    public int hashCode() {
        return classRole.hashCode();
    }
}
