package com.infomaximum.cluster.component.custom.remote.future;

import com.infomaximum.cluster.anotation.DisableValidationRemoteMethod;
import com.infomaximum.cluster.core.remote.struct.RController;

import java.util.concurrent.CompletableFuture;

/**
 * Created by kris on 28.10.16.
 */
public interface RControllerFuture extends RController {

	public CompletableFuture<String> get(String value, long time);

	public CompletableFuture<String> getError(String value, long time);

}
