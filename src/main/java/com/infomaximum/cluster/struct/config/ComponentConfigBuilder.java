package com.infomaximum.cluster.struct.config;

import net.minidev.json.JSONObject;

/**
 * Created by kris on 08.07.17.
 */
public interface ComponentConfigBuilder {

    public void withJSON(JSONObject data);

    public ComponentConfig build();
}
