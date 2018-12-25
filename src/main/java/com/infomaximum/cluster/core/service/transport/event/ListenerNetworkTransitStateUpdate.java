package com.infomaximum.cluster.core.service.transport.event;

import com.infomaximum.cluster.core.service.transport.struct.NetworkTransitState;

public interface ListenerNetworkTransitStateUpdate {

    void update(NetworkTransitState state);
}
