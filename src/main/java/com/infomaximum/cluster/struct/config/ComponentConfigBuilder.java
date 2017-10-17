package com.infomaximum.cluster.struct.config;

import net.minidev.json.JSONObject;

/**
 * Created by kris on 08.07.17.
 */
public interface ComponentConfigBuilder {

    void withJSON(JSONObject data);

    ComponentConfig build();
}
