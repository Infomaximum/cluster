package com.infomaximum.cluster.core.service.transport.struct.packet;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;

import java.io.IOException;

/**
 * Created by kris on 14.09.16.
 */
public abstract class TPacket {

	private final JSONObject data;

	public TPacket(JSONObject data) {
		this.data = data;
	}

	public abstract TypeTPacket getType();

	public JSONObject getData() {
		return data;
	}

	public String serialize() throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", getType().getId());
		serializeNative(jsonObject);
		if (data!=null) jsonObject.put("data", data);
		return jsonObject.toJSONString(JSONStyle.MAX_COMPRESS);
	}

	protected abstract void serializeNative(JSONObject jsonObject) throws IOException;

	public static TPacket deserialize(JSONObject parse) throws IOException, ClassNotFoundException {
		TypeTPacket type = TypeTPacket.get(parse.getAsNumber("type").intValue());
		JSONObject data = (JSONObject) parse.get("data");
		if (type== TypeTPacket.REQUEST) {
			return TPacketRequest.deserialize(parse, data);
		} else if (type== TypeTPacket.RESPONSE) {
			return TPacketResponse.deserialize(parse, data);
		} else {
			throw new RuntimeException("Nothing type packet: " + type);
		}
	}
}
