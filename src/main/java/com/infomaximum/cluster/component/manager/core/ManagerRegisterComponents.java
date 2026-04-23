package com.infomaximum.cluster.component.manager.core;

import com.infomaximum.cluster.Node;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.manager.event.EventUpdateComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
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

    private final NotificationsComponentService notificationsComponentService;

    private final AtomicInteger ids;

    public ManagerRegisterComponents(ManagerComponent managerComponent) {
        this.managerComponent = managerComponent;
        this.managerRuntimeComponent = managerComponent.getTransport().getNetworkTransit().getManagerRuntimeComponent();
        this.notificationsComponentService = new NotificationsComponentService();
        this.ids = new AtomicInteger(0);

        //Регистрируем себя — без уведомления.
        managerRuntimeComponent.getLocalManagerRuntimeComponent().registerComponent(
                new RuntimeComponentInfo(
                        managerComponent.getId(),
                        managerComponent.getInfo().getUuid(),
                        managerComponent.getInfo().getVersion(),
                        managerComponent.getTransport().getExecutor().getClassRControllers()
                ));
    }

    /**
     * Выделяет id и помещает компонент в локальный реестр без оповещения подписчиков.
     * Уведомление слушателей производится отдельным шагом в {@link #startLocalComponent(int)}.
     *
     * @param value данные регистрируемого компонента (поле {@code id} будет перезаписано выделенным значением)
     * @return состояние регистрации с выделенным {@code id}
     */
    public RegistrationState registerLocalComponent(RuntimeComponentInfo value) {
        int nextId = ids.incrementAndGet();
        RuntimeComponentInfo runtimeComponentInfo = RuntimeComponentInfo.upgrade(nextId, value);
        managerRuntimeComponent.getLocalManagerRuntimeComponent().registerComponent(runtimeComponentInfo);
        return new RegistrationState(nextId);
    }

    /**
     * Завершает регистрацию компонента: уведомляет слушателей.
     *
     * @param componentId идентификатор компонента, выделенный в {@link #registerLocalComponent(RuntimeComponentInfo)}
     * @throws IllegalStateException если компонент с таким id отсутствует в локальном реестре
     */
    public void startLocalComponent(int componentId) {
        RuntimeComponentInfo info = managerRuntimeComponent.getLocalManagerRuntimeComponent().get(componentId);
        if (info == null) {
            throw new IllegalStateException("Component is not registered: " + componentId);
        }
        managerRuntimeComponent.getLocalManagerRuntimeComponent().notifyRegistered(info);
        if (!info.uuid.equals(ManagerComponent.UUID)) {
            notificationsComponentService.registerLocalComponent(info);
        }
    }

    public void registerRemoteComponent(Node node, RuntimeComponentInfo value) {
        notificationsComponentService.registerRemoteComponent(node, value);
    }

    public void startRemoteComponent(Node node, RuntimeComponentInfo value) {
        notificationsComponentService.startRemoteComponent(node, value);
    }

    public void unRegisterLocalComponent(RuntimeComponentInfo value) {
        if (managerRuntimeComponent.getLocalManagerRuntimeComponent().unRegisterComponent(value.id)) {
            notificationsComponentService.unRegisterLocalComponent(value);
        }
    }

    public void unRegisterRemoteComponent(Node node, RuntimeComponentInfo value) {
        notificationsComponentService.unRegisterRemoteComponent(node, value);
    }

    public void addListener(EventUpdateComponent eventUpdateComponent) {
        notificationsComponentService.addListener(eventUpdateComponent);
    }

    public void removeListener(EventUpdateComponent eventUpdateComponent) {
        notificationsComponentService.removeListener(eventUpdateComponent);
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
