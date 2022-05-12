package com.infomaximum.cluster.exception;

public class ExceptionBuilder {

    public Exception buildTransitRequestException(int node, int componentUniqueId, Exception cause) {
        return new ClusterException(cause);
    }

    public Exception buildRemoteComponentUnavailableException(int node, int componentUniqueId, Exception cause) {
        return new ClusterException(cause);
    }
}
