package com.infomaximum.cluster.core.service.transport.network;

public interface RemoteControllerRequest {

    Object request(int targetComponentUniqueId, String rControllerClassName, String methodName, Object[] args) throws Exception;
}
