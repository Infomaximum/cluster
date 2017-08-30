package com.infomaximum.cluster.core.service.transport.struct.packet;

import net.minidev.json.JSONObject;

/**
 * Created by kris on 14.09.16.
 */
public class TPacketRequest extends TPacket {

	private final String controller;
	private final String action;

	public TPacketRequest(String controller, String action, JSONObject data) {
		super(data);
		this.controller=controller;
		this.action=action;
	}

	public String getController() {
		return controller;
	}
	public String getAction() {
		return action;
	}

	@Override
	public TypeTPacket getType() {
		return TypeTPacket.REQUEST;
	}

	@Override
	protected void serializeNative(JSONObject jsonObject) {
		jsonObject.put("controller", controller);
		jsonObject.put("action", action);
	}

	public static TPacketRequest deserialize(JSONObject parse, JSONObject data) {
		String controller = parse.getAsString("controller");
		String action = parse.getAsString("action");
		return new TPacketRequest(controller, action, data);
	}
}
