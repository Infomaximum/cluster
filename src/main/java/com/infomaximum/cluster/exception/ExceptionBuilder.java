package com.infomaximum.cluster.exception;

public class ExceptionBuilder {

    public Exception buildTransitRequestException(int node, int componentUniqueId, String rControllerClassName, String methodName, Exception cause) {
        return new ClusterException("TransitRequestException, node: " + node + ", componentUniqueId: "
                + componentUniqueId + ", rControllerClassName: " + rControllerClassName
                + ", methodName: " + methodName, cause
        );
    }

    public Exception buildRemoteComponentUnavailableException(int node, int componentUniqueId, String rControllerClassName, String methodName, Exception cause) {
        return new ClusterException("RemoteComponentUnavailableException, node: " + node + ", componentUniqueId: "
                + componentUniqueId + ", rControllerClassName: " + rControllerClassName
                + ", methodName: " + methodName, cause
        );
    }

    public Exception buildRemoteComponentNotFoundException(int node, int componentUniqueId) {
        return new ClusterException("RemoteComponentUnavailableException, node: " + node + ", componentUniqueId: "
                + componentUniqueId
        );
    }

    public Exception buildMismatchRemoteApiNotFoundControllerException(int node, int componentUniqueId, String rControllerClassName) {
        return new ClusterException("Mismatch api (not found controller), node: " + node + ", componentUniqueId: "
                + componentUniqueId + ", rControllerClassName: " + rControllerClassName
        );
    }

    public Exception buildMismatchRemoteApiNotFoundMethodException(int node, int componentUniqueId, String rControllerClassName, String methodName) {
        return new ClusterException("Mismatch api (not found method), node: " + node + ", componentUniqueId: "
                + componentUniqueId + ", rControllerClassName: " + rControllerClassName
                + ", methodName: " + methodName
        );
    }
}
