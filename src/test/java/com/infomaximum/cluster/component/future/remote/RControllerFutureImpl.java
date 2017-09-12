package com.infomaximum.cluster.component.future.remote;

import com.infomaximum.cluster.component.future.FutureComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.memory.remote.RControllerMemory;
import com.infomaximum.cluster.core.remote.AbstractRController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by kris on 28.10.16.
 */
public class RControllerFutureImpl extends AbstractRController<FutureComponent> implements RControllerFuture {

	public RControllerFutureImpl(FutureComponent component) {
		super(component);
	}

	@Override
	public Future<String> get(String key) {
		return null;
	}
}
