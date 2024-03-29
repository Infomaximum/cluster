package com.infomaximum.cluster.component.custom1.remote.exception;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;

import java.nio.file.NotLinkException;

/**
 * Created by kris on 28.10.16.
 */
public interface RControllerException extends RController {

    public String getException(String value) throws NotLinkException, ClusterException;

}
