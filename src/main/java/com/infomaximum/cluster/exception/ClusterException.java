package com.infomaximum.cluster.exception;

public class ClusterException extends RuntimeException {

    public ClusterException() {}

    public ClusterException(String message) {
        super(message);
    }

    public ClusterException(Throwable cause) {
        super(cause);
    }

    public ClusterException(String message, Throwable cause) {
        super(message, cause);
    }
}
