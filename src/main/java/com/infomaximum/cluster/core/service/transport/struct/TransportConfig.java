package com.infomaximum.cluster.core.service.transport.struct;

import net.minidev.json.JSONObject;

/**
 * Created by kris on 12.09.16.
 */
public class TransportConfig {

	public final String url;

	public TransportConfig(JSONObject jsonObject) {
		this.url = jsonObject.getAsString("url");
	}

}
