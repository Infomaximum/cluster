package com.infomaximum.cluster.core.service.transport.network.local;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.ManagerRuntimeComponent;
import com.infomaximum.cluster.core.service.transport.network.local.event.EventUpdateLocalComponent;
import com.infomaximum.cluster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalManagerRuntimeComponent implements ManagerRuntimeComponent {

    private final Map<Integer, RuntimeComponentInfo> components;

    private final List<EventUpdateLocalComponent> listeners;

    public LocalManagerRuntimeComponent() {
        this.components = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public void registerComponent(RuntimeComponentInfo subSystemInfo) {
        int uniqueId = subSystemInfo.uniqueId;
        String uuid = subSystemInfo.uuid;
        boolean isSingleton = subSystemInfo.isSingleton;

        if (components.containsKey(uniqueId)) {
            throw new RuntimeException();
        }

        synchronized (components) {
            //Первым делом проверем на уникальность.
            if (isSingleton && components.containsKey(uuid)) {
                throw new RuntimeException("Component: " + uuid + " does not support clustering");
            }

            //Регистрируем
            components.put(uniqueId, subSystemInfo);
        }

        //Оповещаем подписчиков
        for (EventUpdateLocalComponent listener : listeners) {
            listener.registerComponent(subSystemInfo);
        }
    }

    @Override
    public boolean unRegisterComponent(int uniqueId) {
        RuntimeComponentInfo removeItem;
        synchronized (components) {
            removeItem = components.remove(uniqueId);
        }

        //Оповещаем подписчиков
        if (removeItem != null) {
            for (EventUpdateLocalComponent listener : listeners) {
                listener.registerComponent(removeItem);
            }
            return true;
        } else {
            return false;
        }
    }

    public void addListener(EventUpdateLocalComponent listener) {
        listeners.add(listener);
    }

    public void removeListener(EventUpdateLocalComponent listener) {
        listeners.remove(listener);
    }

    @Override
    public Collection<RuntimeComponentInfo> getComponents() {
        //TODO - нужна оптимизациия - странно каждый раз формировать этот лист
        return components.values();
    }

    @Override
    public RuntimeComponentInfo get(int uniqueId) {
        return components.get(uniqueId);
    }

    @Override
    public RuntimeComponentInfo find(String uuid, Class<? extends RController> remoteControllerClazz) {
        List<RuntimeComponentInfo> items = new ArrayList<>();
        for (Map.Entry<Integer, RuntimeComponentInfo> entry : components.entrySet()) {
            RuntimeComponentInfo runtimeComponentInfo = entry.getValue();
            String runtimeComponentUuid = runtimeComponentInfo.uuid;
            if (runtimeComponentUuid.equals(uuid)) {
                items.add(runtimeComponentInfo);
            }
        }
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(RandomUtil.random.nextInt(items.size()));
        }
    }

    @Override
    public Collection<RuntimeComponentInfo> find(Class<? extends RController> remoteControllerClazz) {
        List<RuntimeComponentInfo> items = new ArrayList<>();
        for (Map.Entry<Integer, RuntimeComponentInfo> entry : components.entrySet()) {
            RuntimeComponentInfo runtimeComponentInfo = entry.getValue();
            if (runtimeComponentInfo.getClassNameRControllers().contains(remoteControllerClazz.getName())) {
                items.add(runtimeComponentInfo);
            }
        }
        return items;
    }
}
