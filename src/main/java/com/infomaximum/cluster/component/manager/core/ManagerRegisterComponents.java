package com.infomaximum.cluster.component.manager.core;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.controller.notification.RControllerNotification;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.struct.RegistrationState;
import com.infomaximum.cluster.utils.GlobalUniqueIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kris on 23.09.16.
 */
public class ManagerRegisterComponents {

    private final static Logger log = LoggerFactory.getLogger(ManagerRegisterComponents.class);

    private final ManagerComponent managerComponent;

    private final ManagerRuntimeComponent managerRuntimeComponent;

    private final AtomicInteger uniqueIds;

    public ManagerRegisterComponents(ManagerComponent managerComponent) {
        this.managerComponent = managerComponent;
        this.managerRuntimeComponent = managerComponent.getTransport().getNetworkTransit().getManagerRuntimeComponent();
        this.uniqueIds = new AtomicInteger(0);

        //Регистрируем себя
        _registerActiveComponent(
                new RuntimeComponentInfo(
                        managerComponent.getRemotes().cluster.node,
                        managerComponent.getUniqueId(),
                        managerComponent.getInfo().getUuid(),
                        managerComponent.isSingleton(),
                        managerComponent.getTransport().getExecutor().getClassRControllers()
                ));
    }

    public RegistrationState registerActiveComponent(RuntimeComponentInfo value) {
        int uniqueId = GlobalUniqueIdUtils.getGlobalUniqueId(
                managerComponent.getRemotes().cluster.node,
                uniqueIds.incrementAndGet()
        );
        RuntimeComponentInfo runtimeComponentInfo = RuntimeComponentInfo.upgrade(uniqueId, value);
        _registerActiveComponent(runtimeComponentInfo);
        return new RegistrationState(uniqueId);
    }

    private void _registerActiveComponent(RuntimeComponentInfo subSystemInfo) {
        String uuid = subSystemInfo.uuid;

        managerRuntimeComponent.registerComponent(subSystemInfo);

        //Оповещаем все подсистемы о новом модуле - кроме ситуации, когда регистрируется менеджер
        if (!uuid.equals(ManagerComponent.UUID)) {
            for (RControllerNotification rControllerNotification : managerComponent.getRemotes().getControllers(RControllerNotification.class)) {
                rControllerNotification.notificationRegisterComponent(subSystemInfo);
            }
        }
    }

    public void unRegisterActiveComponent(int uniqueId) {
        if (managerRuntimeComponent.unRegisterComponent(uniqueId)) {
            //Oповещаем все подсистемы
            for (RControllerNotification rControllerNotification : managerComponent.getRemotes().getControllers(RControllerNotification.class)) {
                rControllerNotification.notificationUnRegisterComponent(uniqueId);
            }
        }
    }

    public RuntimeComponentInfo get(int uniqueId) {
        return managerRuntimeComponent.get(uniqueId);
    }

    public RuntimeComponentInfo find(String uuid, Class<? extends RController> remoteControllerClazz) {
        return managerRuntimeComponent.find(uuid, remoteControllerClazz);
    }

    public Collection<RuntimeComponentInfo> find(Class<? extends RController> remoteControllerClazz) {
        return managerRuntimeComponent.find(remoteControllerClazz);
    }

    public Collection<RuntimeComponentInfo> getComponents() {
        return managerRuntimeComponent.getComponents();
    }
}
