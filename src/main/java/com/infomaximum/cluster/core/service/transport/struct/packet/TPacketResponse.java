package com.infomaximum.cluster.core.service.transport.struct.packet;

import com.infomaximum.cluster.utils.SerializeException;
import net.minidev.json.JSONObject;

import java.io.IOException;

/**
 * Created by kris on 14.09.16.
 */
public class TPacketResponse extends TPacket {

	public static final String CODE_SUCCESS="SUCCESS";
	public static final String CODE_EXCEPTION="EXCEPTION";

	private final String code;
	private final Exception exception;

	public TPacketResponse(JSONObject data) {
		this(CODE_SUCCESS, null, data);
	}

	@Override
	public TypeTPacket getType() {
		return TypeTPacket.RESPONSE;
	}

	public TPacketResponse(Exception exception, JSONObject data) {
		this(CODE_EXCEPTION, exception, data);
	}

	public TPacketResponse(String code, Exception exception, JSONObject data) {
		super(data);
		this.code = code;
		this.exception = exception;
	}

	public String getCode() {
		return code;
	}
	public Exception getException() {
		return exception;
	}
	public boolean isSuccess() {
		return CODE_SUCCESS.equals(code);
	}

	@Override
	protected void serializeNative(JSONObject jsonObject) throws IOException {
		jsonObject.put("code", code);
		if (exception!=null) {
			jsonObject.put("exception", SerializeException.serialize(exception));
		}
	}

	public static TPacketResponse deserialize(JSONObject parse, JSONObject data) throws IOException, ClassNotFoundException {
		String code = parse.getAsString("code");
		Exception exception = (parse.containsKey("exception"))? SerializeException.deserialize(parse.getAsString("exception")):null;
		return new TPacketResponse(code, exception, data);
	}
}
