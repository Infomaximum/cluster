package com.infomaximum.cluster.component.memory.remote;

import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.core.remote.AbstractRController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kris on 28.10.16.
 */
public class RControllerMemoryImpl extends AbstractRController<MemoryComponent> implements RControllerMemory {

	public RControllerMemoryImpl(MemoryComponent memoryComponent) {
		super(memoryComponent);
	}

	@Override
	public Serializable get(String key) {
		return component.getMemoryEngine().get(key);
	}

	@Override
	public void set(String key, Serializable value) {
		component.getMemoryEngine().set(key, value);
	}

	@Override
	public void clear(String key) {
		component.getMemoryEngine().set(key, null);
	}

	@Override
	public Map<String, Serializable> gets(String... keys) {
		Map<String, Serializable> result = new HashMap<>();
		for (String key: keys) {
			Serializable value = component.getMemoryEngine().get(key);
			result.put(key, value);
		}
		return result;
	}


	@Override
	public void sets(Map<String, Serializable> values) {
		for (Map.Entry<String, Serializable> entry: values.entrySet()) {
			component.getMemoryEngine().set(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear(List<String> keys) {
		for (String key: keys) {
			component.getMemoryEngine().set(key, null);
		}
	}
}
