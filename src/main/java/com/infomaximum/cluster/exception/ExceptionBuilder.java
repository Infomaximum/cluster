package com.infomaximum.cluster.exception;

public interface ExceptionBuilder<T extends Exception> {
    Class getTypeException();

    T buildTransitRequestException(int node, int componentUniqueId, String rControllerClassName, int methodKey, Exception cause);

    T buildRemoteComponentUnavailableException(int node, int componentUniqueId, String rControllerClassName, int methodKey, Exception cause);

    T buildRemoteComponentNotFoundException(int node, int componentUniqueId);

    T buildMismatchRemoteApiNotFoundControllerException(int node, int componentUniqueId, String rControllerClassName);

    T buildMismatchRemoteApiNotFoundMethodException(int node, int componentUniqueId, String rControllerClassName, int methodKey);
}
