package com.infomaximum.cluster;

import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.struct.config.ComponentConfig;

import java.lang.reflect.Constructor;

/**
 * Created by kris on 19.06.17.
 */
public class ComponentBuilder {

    private final Class<? extends Component> componentClass;

    private ComponentConfig config;

    public ComponentBuilder(Class<? extends Component> componentClass) {
        this.componentClass = componentClass;
    }

    public ComponentBuilder withConfig(ComponentConfig config) {
        this.config = config;
        return this;
    }

    public Class<? extends Component> getComponentClass() {
        return componentClass;
    }

    public ComponentConfig getConfig() {
        return config;
    }

    protected Component build() throws ClusterException {
        try {
            Constructor constructor = componentClass.getConstructor(ComponentConfig.class);
            if (constructor == null) {
                throw new ClusterException("Not found constructor in component: " + componentClass);
            }

            return (Component) constructor.newInstance(config);
        } catch (ReflectiveOperationException ex) {
            throw new ClusterException(ex);
        }
    }
}
