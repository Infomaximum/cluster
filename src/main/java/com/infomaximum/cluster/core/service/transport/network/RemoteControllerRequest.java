package com.infomaximum.cluster.core.service.transport.network;

import com.infomaximum.cluster.core.service.transport.executor.ComponentExecutorTransport;
import com.infomaximum.cluster.struct.Component;

public interface RemoteControllerRequest {

    ComponentExecutorTransport.Result request(Component sourceComponent, int targetComponentUniqueId, String rControllerClassName, int methodKey, byte[][] args) throws Exception;
}
