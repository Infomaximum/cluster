package com.infomaximum.cluster.component.manager.core;

import com.infomaximum.cluster.Node;
import com.infomaximum.cluster.component.manager.event.EventUpdateComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationsComponentService {

    private final List<EventUpdateComponent> listeners;

    public NotificationsComponentService() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void registerLocalComponent(RuntimeComponentInfo subSystemInfo) {
        for (EventUpdateComponent listener : listeners) {
            listener.registerLocalComponent(subSystemInfo);
        }
    }

    public void unRegisterLocalComponent(RuntimeComponentInfo subSystemInfo) {
        for (EventUpdateComponent listener : listeners) {
            listener.unRegisterLocalComponent(subSystemInfo);
        }
    }

    public void registerRemoteComponent(Node node, RuntimeComponentInfo subSystemInfo) {
        for (EventUpdateComponent listener : listeners) {
            listener.registerRemoteComponent(node, subSystemInfo);
        }
    }

    public void unRegisterRemoteComponent(Node node, RuntimeComponentInfo subSystemInfo) {
        for (EventUpdateComponent listener : listeners) {
            listener.unRegisterRemoteComponent(node, subSystemInfo);
        }
    }

    public void startRemoteComponent(Node node, RuntimeComponentInfo subSystemInfo) {
        for (EventUpdateComponent listener : listeners) {
            listener.startRemoteComponent(node, subSystemInfo);
        }
    }

    public void addListener(EventUpdateComponent eventUpdateComponent) {
        listeners.add(eventUpdateComponent);
    }

    public void removeListener(EventUpdateComponent eventUpdateComponent) {
        listeners.remove(eventUpdateComponent);
    }
}
