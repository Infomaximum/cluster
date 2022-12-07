package com.infomaximum.cluster.exception;

public class ExceptionBuilder {

    public Exception buildTransitRequestException(int node, int componentUniqueId, String rControllerClassName, String methodName, Exception cause) {
        return new ClusterException(
                "TransitRequestException, node: " + node + ", componentUniqueId: "
                        + componentUniqueId + ", rControllerClassName: " + rControllerClassName
                        + ", methodName: " + methodName, cause);
    }

    public Exception buildRemoteComponentUnavailableException(int node, int componentUniqueId, String rControllerClassName, String methodName, Exception cause) {
        return new ClusterException(
                "RemoteComponentUnavailableException, node: " + node + ", componentUniqueId: "
                        + componentUniqueId + ", rControllerClassName: " + rControllerClassName
                        + ", methodName: " + methodName, cause);
    }
}
