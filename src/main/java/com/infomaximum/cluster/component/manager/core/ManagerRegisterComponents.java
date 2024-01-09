package com.infomaximum.cluster.component.manager.core;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.controller.notification.RControllerNotification;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.LocationRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.struct.RegistrationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kris on 23.09.16.
 */
public class ManagerRegisterComponents {

    private final static Logger log = LoggerFactory.getLogger(ManagerRegisterComponents.class);

    private final ManagerComponent managerComponent;

    private final ManagerRuntimeComponent managerRuntimeComponent;

    private final AtomicInteger ids;

    public ManagerRegisterComponents(ManagerComponent managerComponent) {
        this.managerComponent = managerComponent;
        this.managerRuntimeComponent = managerComponent.getTransport().getNetworkTransit().getManagerRuntimeComponent();
        this.ids = new AtomicInteger(0);

        //Регистрируем себя
        _registerActiveComponent(
                new RuntimeComponentInfo(
                        managerComponent.getId(),
                        managerComponent.getInfo().getUuid(),
                        managerComponent.getTransport().getExecutor().getClassRControllers()
                ));
    }

    public RegistrationState registerActiveComponent(RuntimeComponentInfo value) {
        int nextId = ids.incrementAndGet();
        RuntimeComponentInfo runtimeComponentInfo = RuntimeComponentInfo.upgrade(nextId, value);
        _registerActiveComponent(runtimeComponentInfo);
        return new RegistrationState(nextId);
    }

    private void _registerActiveComponent(RuntimeComponentInfo subSystemInfo) {
        String uuid = subSystemInfo.uuid;

        managerRuntimeComponent.getLocalManagerRuntimeComponent().registerComponent(subSystemInfo);

        //Оповещаем все подсистемы о новом модуле - кроме ситуации, когда регистрируется менеджер
        if (!uuid.equals(ManagerComponent.UUID)) {
            for (RControllerNotification rControllerNotification : managerComponent.getRemotes().getControllers(RControllerNotification.class)) {
                rControllerNotification.notificationRegisterComponent(subSystemInfo);
            }
        }
    }

    public void unRegisterActiveComponent(int componentId) {
        if (managerRuntimeComponent.getLocalManagerRuntimeComponent().unRegisterComponent(componentId)) {
            //Oповещаем все подсистемы
            for (RControllerNotification rControllerNotification : managerComponent.getRemotes().getControllers(RControllerNotification.class)) {
                rControllerNotification.notificationUnRegisterComponent(componentId);
            }
        }
    }

    public LocationRuntimeComponent getLocationRuntimeComponent(UUID nodeRuntimeId, int componentId) {
        return managerComponent.getTransport().getNetworkTransit().getManagerRuntimeComponent().get(nodeRuntimeId, componentId);
    }

    public Collection<LocationRuntimeComponent> getLocationRuntimeComponents(UUID nodeRuntimeId) {
        return managerComponent.getTransport().getNetworkTransit().getManagerRuntimeComponent().gets(nodeRuntimeId);
    }

    public RuntimeComponentInfo getLocalComponent(int componentId) {
        return managerRuntimeComponent.getLocalManagerRuntimeComponent().get(componentId);
    }

    public RuntimeComponentInfo getLocalComponent(String uuid) {
        return managerRuntimeComponent.getLocalManagerRuntimeComponent().find(uuid);
    }

    public Collection<RuntimeComponentInfo> findLocalComponent(Class<? extends RController> remoteControllerClazz) {
        return managerRuntimeComponent.getLocalManagerRuntimeComponent().find(remoteControllerClazz);
    }

    public LocationRuntimeComponent find(String uuid) {
        return managerRuntimeComponent.find(uuid);
    }

    public Collection<LocationRuntimeComponent> find(Class<? extends RController> remoteControllerClazz) {
        return managerRuntimeComponent.find(remoteControllerClazz);
    }

    public Collection<RuntimeComponentInfo> getLocalComponents() {
        return managerRuntimeComponent.getLocalManagerRuntimeComponent().getComponents();
    }
}
