package com.infomaximum.cluster.core.service.transport.network;

import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.Method;

public interface RemoteControllerRequest {

    Object request(Component sourceComponent, int targetComponentUniqueId, String rControllerClassName, Method method, Object[] args) throws Exception;
}
