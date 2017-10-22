package com.infomaximum.cluster.exception;

public class ClusterException extends Exception {

    public ClusterException() {}

    public ClusterException(String message) {
        super(message);
    }

    public ClusterException(Throwable cause) {
        super(cause);
    }
}
