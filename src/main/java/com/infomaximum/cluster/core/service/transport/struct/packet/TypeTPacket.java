package com.infomaximum.cluster.core.service.transport.struct.packet;

/**
 * Created by kris on 14.09.16.
 */
public enum TypeTPacket {

	REQUEST(1),

	RESPONSE(2);

	private final int id;

	private TypeTPacket(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TypeTPacket get(int id) {
		for(TypeTPacket item: TypeTPacket.values()) {
			if (item.getId()==id) return item;
		}
		throw new RuntimeException("Nothing type: " + id);
	}
}
