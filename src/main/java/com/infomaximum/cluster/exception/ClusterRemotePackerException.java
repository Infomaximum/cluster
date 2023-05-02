package com.infomaximum.cluster.exception;

/**
 * Created by user on 06.09.2017.
 */
public class ClusterRemotePackerException extends ClusterRemoteException {

    public ClusterRemotePackerException() {
    }

    public ClusterRemotePackerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClusterRemotePackerException(Throwable cause) {
        super(cause);
    }
}
