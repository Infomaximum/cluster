package com.infomaximum.cluster.core.service.transport.network.local;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.local.event.EventUpdateLocalComponent;
import com.infomaximum.cluster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalManagerRuntimeComponent {

    private final Map<Integer, RuntimeComponentInfo> components;

    private final List<EventUpdateLocalComponent> listeners;

    public LocalManagerRuntimeComponent() {
        this.components = new ConcurrentHashMap<>();
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void registerComponent(RuntimeComponentInfo componentInfo) {
        int id = componentInfo.id;

        synchronized (components) {
            if (components.containsKey(id)) {
                throw new RuntimeException();
            }
            components.put(id, componentInfo);
        }

        //Оповещаем подписчиков
        for (EventUpdateLocalComponent listener : listeners) {
            listener.registerComponent(componentInfo);
        }
    }

    public boolean unRegisterComponent(int id) {
        RuntimeComponentInfo removeItem;
        synchronized (components) {
            removeItem = components.remove(id);
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

    public Collection<RuntimeComponentInfo> getComponents() {
        //TODO - нужна оптимизациия - странно каждый раз формировать этот лист
        return components.values();
    }

    public RuntimeComponentInfo get(int id) {
        return components.get(id);
    }

    public RuntimeComponentInfo find(String uuid) {
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
