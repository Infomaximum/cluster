package com.infomaximum.cluster.component.memory.remote;

import com.infomaximum.cluster.core.remote.struct.RController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kris on 28.10.16.
 */
public interface RControllerMemory extends RController {

	public Serializable get(String key);

	public void set(String key, Serializable value);

	public void clear(String key);


	public Map<String, Serializable> gets(String... keys);

	public void sets(Map<String, Serializable> values);

	public void clear(List<String> keys);
}
