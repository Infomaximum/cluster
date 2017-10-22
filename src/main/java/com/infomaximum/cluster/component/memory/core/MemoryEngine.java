package com.infomaximum.cluster.component.memory.core;

import com.infomaximum.cluster.component.memory.MemoryComponent;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kris on 17.10.16.
 */
public class MemoryEngine {

	public final MemoryComponent memoryComponent;

	private final Map<String, Serializable> memory;

	public MemoryEngine(MemoryComponent memoryComponent) {
		this.memoryComponent = memoryComponent;
		this.memory = new ConcurrentHashMap<>();
	}

	public void set(String key, Serializable value) {
		if (value!=null) {
			memory.put(key, value);
		} else {
			memory.remove(key);
		}
	}

	public Serializable get(String key) {
		return memory.get(key);
	}
}
