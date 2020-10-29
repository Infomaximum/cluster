package com.infomaximum.cluster.component.manager.core;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.component.environment.EnvironmentComponents;
import com.infomaximum.cluster.core.remote.controller.notification.RControllerNotification;
import com.infomaximum.cluster.struct.RegistrationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by kris on 23.09.16.
 */
public class ManagerRegisterComponents implements EnvironmentComponents {

    private final static Logger log = LoggerFactory.getLogger(ManagerRegisterComponents.class);

    private final ManagerComponent managerComponent;

    private final Map<Integer, RuntimeComponentInfo> components;

    private final AtomicInteger uniqueIds;

    public ManagerRegisterComponents(ManagerComponent managerComponent) {
        this.managerComponent = managerComponent;
        this.components = new ConcurrentHashMap<>();
        this.uniqueIds = new AtomicInteger(0);

        //Регистрируем себя
        registerActiveRole(
                managerComponent.getUniqueId(),
                new RuntimeComponentInfo(
                        managerComponent.getUniqueId(),
                        managerComponent.getInfo(),
                        managerComponent.isSingleton(),
                        managerComponent.getTransport().getExecutor().getClassRControllers()
                ));
    }

    public RegistrationState registerActiveRole(RuntimeComponentInfo value) {
        int uniqueId = uniqueIds.incrementAndGet();
        RuntimeComponentInfo runtimeComponentInfo = RuntimeComponentInfo.upgrade(uniqueId, value);
        ArrayList<RuntimeComponentInfo> items = registerActiveRole(uniqueId, runtimeComponentInfo);
        return new RegistrationState(uniqueId, items);
    }

    private ArrayList<RuntimeComponentInfo> registerActiveRole(int uniqueId, RuntimeComponentInfo subSystemInfo) {
        String uuid = subSystemInfo.info.getUuid();
        boolean isSingleton = subSystemInfo.isSingleton;

        if (components.containsKey(uniqueId)) {
            throw new RuntimeException();
        }

        Map<Integer, RuntimeComponentInfo> activeSubSystems;
        synchronized (components) {
            //Первым делом проверем на уникальность.
            if (isSingleton && getActiveComponentUUIDs().contains(uuid)) {
                throw new RuntimeException("Component: " + uuid + " does not support clustering");
            }

            //Сохраняем список подсистем которые необходимо оповестить
            activeSubSystems = new HashMap<>(components);

            //Регистрируем
            components.put(uniqueId, subSystemInfo);
        }

        //Оповещаем все подсистемы о новом модуле
        for (int iUniqueId : activeSubSystems.keySet()) {
            managerComponent.getRemotes().getFromCKey(iUniqueId, RControllerNotification.class).notificationRegisterComponent(subSystemInfo);
        }

        return new ArrayList<>(components.values());
    }

    public Collection<RuntimeComponentInfo> unRegisterActiveRole(int uniqueId) {
        if (!components.containsKey(uniqueId)) return components.values();

        synchronized (components) {
            components.remove(uniqueId);
        }

        //Oповещаем все подсистемы
        for (Integer iUniqueId : components.keySet()) {
            managerComponent.getRemotes().getFromCKey(iUniqueId, RControllerNotification.class).notificationUnRegisterComponent(uniqueId);
        }

        return components.values();
    }

    public Collection<RuntimeComponentInfo> getActiveComponents() {
        return components.values();
    }

    public Collection<Integer> getActiveComponentUniqueIds() {
        return new HashSet<Integer>(components.keySet());
    }

    public Collection<String> getActiveComponentUUIDs() {
        return components.values().stream()
                .map(runtimeComponentInfo -> runtimeComponentInfo.info.getUuid())
                .collect(Collectors.toSet());
    }

    public Collection<String> getActiveComponentUuids() {
        HashSet<String> subSystemUuids = new HashSet<String>();
        for (RuntimeComponentInfo subSystemInfo : components.values()) {
            subSystemUuids.add(subSystemInfo.info.getUuid());
        }
        return subSystemUuids;
    }

    public boolean isActiveComponent(String uuid) {
        return getActiveComponentUuids().contains(uuid);
    }
}
