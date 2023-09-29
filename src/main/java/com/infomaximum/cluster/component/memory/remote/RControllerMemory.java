package com.infomaximum.cluster.component.memory.remote;

import com.infomaximum.cluster.core.remote.struct.RController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kris on 28.10.16.
 */
public interface RControllerMemory extends RController {

	Serializable get(String key) throws Exception;

    void set(String key, Serializable value) throws Exception;

    void clear(String key) throws Exception;


    HashMap<String, Serializable> gets(String... keys) throws Exception;

    void sets(HashMap<String, Serializable> values) throws Exception;

    void clears(ArrayList<String> keys) throws Exception;
}
