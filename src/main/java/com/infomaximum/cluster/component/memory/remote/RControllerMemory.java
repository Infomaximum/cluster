package com.infomaximum.cluster.component.memory.remote;

import com.infomaximum.cluster.core.remote.struct.RController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kris on 28.10.16.
 */
public interface RControllerMemory extends RController {

	Serializable get(String key);

	void set(String key, Serializable value);

	void clear(String key);


	Map<String, Serializable> gets(String... keys);

	void sets(Map<String, Serializable> values);

	void clear(List<String> keys);
}
