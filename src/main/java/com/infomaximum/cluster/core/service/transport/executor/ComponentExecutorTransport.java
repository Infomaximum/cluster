package com.infomaximum.cluster.core.service.transport.executor;

import com.infomaximum.cluster.core.remote.struct.RController;

import java.util.HashSet;

public interface ComponentExecutorTransport {

    public record Result(byte[] value, byte[] exception) {
    }

    public HashSet<Class<? extends RController>> getClassRControllers();

    public Object execute(String rControllerClassName, String methodName, Object[] args) throws Exception;

    public Result execute(String rControllerClassName, String methodName, byte[][] byteArgs) throws Exception;
}
