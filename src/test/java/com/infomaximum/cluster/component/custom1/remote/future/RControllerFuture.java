package com.infomaximum.cluster.component.custom1.remote.future;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;

import java.util.concurrent.CompletableFuture;

/**
 * Created by kris on 28.10.16.
 */
public interface RControllerFuture extends RController {

    public CompletableFuture<String> get(String value, long time) throws ClusterException;

    public CompletableFuture<String> getError(String value, long time) throws ClusterException;

}
