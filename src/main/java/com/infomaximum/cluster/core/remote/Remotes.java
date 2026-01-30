package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.LocationRuntimeComponent;
import com.infomaximum.cluster.struct.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by kris on 28.10.16.
 */
public class Remotes {

    private final static Logger log = LoggerFactory.getLogger(Remotes.class);

    public final Cluster cluster;
    public final Component component;

    private final ManagerComponent managerComponent;
    private final ComponentRemotePacker componentRemotePacker;

    public Remotes(Cluster cluster, Component component) {
        this.cluster = cluster;
        this.component = component;

        this.managerComponent = (component instanceof ManagerComponent) ? (ManagerComponent) component : cluster.getAnyLocalComponent(ManagerComponent.class);
        this.componentRemotePacker = new ComponentRemotePacker(this);
    }

    public ComponentRemotePacker getRemotePackerObjects() {
        return componentRemotePacker;
    }

    public <T extends RController> T getFromCKey(RemoteTarget target, Class<T> remoteControllerClazz) {
        //Кешировать proxy remoteController нельзя т.к. Proxy.newProxyInstance может вернуться переиспользуемый объект в котором _properties уже заполнен и мы иего перезатрем
        RController remoteController = (RController) Proxy.newProxyInstance(
                remoteControllerClazz.getClassLoader(), new Class[]{remoteControllerClazz},
                new RemoteControllerInvocationHandler(component, target, remoteControllerClazz)
        );

        return (T) remoteController;
    }

    public <T extends RController> T get(String uuid, Class<T> remoteControllerClazz) {
        LocationRuntimeComponent runtimeComponent = managerComponent.getRegisterComponent().find(uuid);
        if (runtimeComponent == null) {
            throw new RuntimeException("Not found: " + remoteControllerClazz.getName() + " in " + uuid);
        }
        RemoteTarget target = new RemoteTarget(runtimeComponent.node(), runtimeComponent.component().id, uuid);
        return getFromCKey(target, remoteControllerClazz);
    }

    public <T extends RController> boolean isController(String uuid, Class<T> remoteControllerClazz) {
        LocationRuntimeComponent runtimeComponent = managerComponent.getRegisterComponent().find(uuid);
        if (runtimeComponent == null) {
            return false;
        }
        return runtimeComponent.component().getClassNameRControllers().contains(remoteControllerClazz.getName());
    }

    public <T extends RController> T get(Class<? extends Component> classComponent, Class<T> remoteControllerClazz) {
        String uuid = cluster.getUuid(classComponent);
        return get(uuid, remoteControllerClazz);
    }

    public <T extends RController> T get(Class<T> clazz) {
        throw new RuntimeException("Not implemented");
    }

    public <T extends RController> Collection<T> getControllers(Class<? extends Component> classComponent, Class<T> classController) {
        throw new RuntimeException("Not implemented");
    }

    public <T extends RController> Collection<T> getControllers(Class<T> remoteClassController) {
        return managerComponent.getRegisterComponent().find(remoteClassController).stream()
                .map(runtimeComponent -> getFromCKey(new RemoteTarget(runtimeComponent.node(), runtimeComponent.component().id, runtimeComponent.component().uuid), remoteClassController))
                .collect(Collectors.toList());
    }

    public <T extends RController> Collection<T> getControllers(UUID nodeUuid, Class<T> remoteClassController) {
        return managerComponent.getRegisterComponent().find(remoteClassController).stream()
                .filter(locationRuntimeComponent -> nodeUuid.equals(locationRuntimeComponent.node()))
                .map(runtimeComponent -> getFromCKey(new RemoteTarget(runtimeComponent.node(), runtimeComponent.component().id, runtimeComponent.component().uuid), remoteClassController))
                .collect(Collectors.toList());
    }
}
