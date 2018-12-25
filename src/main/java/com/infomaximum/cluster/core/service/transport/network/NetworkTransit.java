package com.infomaximum.cluster.core.service.transport.network;

import com.infomaximum.cluster.core.service.transport.event.ListenerNetworkTransitStateUpdate;
import com.infomaximum.cluster.core.service.transport.struct.NetworkTransitState;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class NetworkTransit {

    private NetworkTransitState state;
    private final List<ListenerNetworkTransitStateUpdate> listeners;

    public NetworkTransit() {
        state = NetworkTransitState.STOPPED;
        this.listeners = new CopyOnWriteArrayList<>();
    }

    protected void setState(NetworkTransitState state) {
        if (this.state == state) return;
        this.state = state;
        for (ListenerNetworkTransitStateUpdate listener : listeners) {
            listener.update(state);
        }
    }

    public void addListener(ListenerNetworkTransitStateUpdate listener) {
        listeners.add(listener);
    }

    public void removeListener(ListenerNetworkTransitStateUpdate listener) {
        listeners.remove(listener);
    }

    public abstract void close();
}
