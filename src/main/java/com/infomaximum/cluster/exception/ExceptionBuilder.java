package com.infomaximum.cluster.exception;

import java.util.UUID;

public interface ExceptionBuilder<T extends Exception> {
    Class getTypeException();

    T buildTransitRequestException(UUID nodeRuntimeId, int componentId, String rControllerClassName, int methodKey, Exception cause);

    T buildRemoteComponentUnavailableException(UUID nodeRuntimeId, int componentId, String rControllerClassName, int methodKey, Exception cause);

    T buildRemoteComponentNotFoundException(UUID nodeRuntimeId, int componentId);

    T buildMismatchRemoteApiNotFoundControllerException(UUID nodeRuntimeId, int componentId, String rControllerClassName);

    T buildMismatchRemoteApiNotFoundMethodException(UUID nodeRuntimeId, int componentId, String rControllerClassName, int methodKey);
}
