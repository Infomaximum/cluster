package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.Collection;
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

    public <T extends RController> T getFromCKey(int componentUniqueId, Class<T> remoteControllerClazz) {
        //Кешировать proxy remoteController нельзя т.к. Proxy.newProxyInstance может вернуться переиспользуемый объект в котором _properties уже заполнен и мы иего перезатрем
        RController remoteController = (RController) Proxy.newProxyInstance(
                remoteControllerClazz.getClassLoader(), new Class[]{remoteControllerClazz},
                new RemoteControllerInvocationHandler(component, componentUniqueId, remoteControllerClazz)
        );

        return (T) remoteController;
    }

    public <T extends RController> T get(String uuid, Class<T> remoteControllerClazz) {
        RuntimeComponentInfo runtimeComponentInfo = managerComponent.getRegisterComponent().find(uuid);
        if (runtimeComponentInfo == null) {
            throw new RuntimeException("Not found: " + remoteControllerClazz.getName() + " in " + uuid);
        }
        return getFromCKey(runtimeComponentInfo.uniqueId, remoteControllerClazz);
    }

    public <T extends RController> boolean isController(String uuid, Class<T> remoteControllerClazz) {
        RuntimeComponentInfo runtimeComponentInfo = managerComponent.getRegisterComponent().find(uuid);
        if (runtimeComponentInfo == null) {
            return false;
        }
        return runtimeComponentInfo.getClassNameRControllers().contains(remoteControllerClazz.getName());
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
                .map(runtimeComponentInfo -> getFromCKey(runtimeComponentInfo.uniqueId, remoteClassController))
                .collect(Collectors.toList());
    }
}
