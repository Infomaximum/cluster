package com.infomaximum.cluster.exception;

public class ExceptionBuilderImpl implements ExceptionBuilder<ClusterException> {

    @Override
    public Class getTypeException() {
        return ClusterException.class;
    }

    @Override
    public ClusterException buildTransitRequestException(int node, int componentUniqueId, String rControllerClassName, int methodKey, Exception cause) {
        return new ClusterException("TransitRequestException, node: " + node + ", componentUniqueId: "
                + componentUniqueId + ", rControllerClassName: " + rControllerClassName
                + ", methodKey: " + methodKey, cause
        );
    }

    @Override
    public ClusterException buildRemoteComponentUnavailableException(int node, int componentUniqueId, String rControllerClassName, int methodKey, Exception cause) {
        return new ClusterException("RemoteComponentUnavailableException, node: " + node + ", componentUniqueId: "
                + componentUniqueId + ", rControllerClassName: " + rControllerClassName
                + ", methodKey: " + methodKey, cause
        );
    }

    @Override
    public ClusterException buildRemoteComponentNotFoundException(int node, int componentUniqueId) {
        return new ClusterException("RemoteComponentUnavailableException, node: " + node + ", componentUniqueId: "
                + componentUniqueId
        );
    }

    @Override
    public ClusterException buildMismatchRemoteApiNotFoundControllerException(int node, int componentUniqueId, String rControllerClassName) {
        return new ClusterException("Mismatch api (not found controller), node: " + node + ", componentUniqueId: "
                + componentUniqueId + ", rControllerClassName: " + rControllerClassName
        );
    }

    @Override
    public ClusterException buildMismatchRemoteApiNotFoundMethodException(int node, int componentUniqueId, String rControllerClassName, int methodKey) {
        return new ClusterException("Mismatch api (not found method), node: " + node + ", componentUniqueId: "
                + componentUniqueId + ", rControllerClassName: " + rControllerClassName
                + ", methodKey: " + methodKey
        );
    }
}
