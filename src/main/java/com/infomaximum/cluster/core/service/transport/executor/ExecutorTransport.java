package com.infomaximum.cluster.core.service.transport.executor;

import com.infomaximum.cluster.core.remote.struct.RController;

import java.util.Collection;

public interface ExecutorTransport {

    public Collection<Class<? extends RController>> getClassRControllers();

    public Object execute(String rControllerClassName, String methodName, Object[] args) throws Exception;
}
