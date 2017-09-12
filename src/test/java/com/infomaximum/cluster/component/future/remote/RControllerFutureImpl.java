package com.infomaximum.cluster.component.future.remote;

import com.infomaximum.cluster.component.future.FutureComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.memory.remote.RControllerMemory;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.utils.ExecutorUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by kris on 28.10.16.
 */
public class RControllerFutureImpl extends AbstractRController<FutureComponent> implements RControllerFuture {

	public RControllerFutureImpl(FutureComponent component) {
		super(component);
	}

	@Override
	public Future<String> get(String value, long time) {
		CompletableFuture<String> future = new CompletableFuture<>();

		if (time==0) {
			future.complete(value);
		} else {
			ExecutorUtils.executors.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(time);
						future.complete(value);
					} catch (InterruptedException e) {
						future.completeExceptionally(e);
					}
				}
			});
		}

		return future;
	}
}
