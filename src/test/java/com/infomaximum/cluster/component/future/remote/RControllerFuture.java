package com.infomaximum.cluster.component.future.remote;

import com.infomaximum.cluster.core.remote.struct.RController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by kris on 28.10.16.
 */
public interface RControllerFuture extends RController {

	public Future<String> get(String key);

}
