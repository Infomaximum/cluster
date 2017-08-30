package com.infomaximum.cluster.utils;

import java.io.*;
import java.util.Base64;

/**
 * Created by kris on 21.09.16.
 */
public class SerializeException {

	public static String serialize(Exception exception) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try(ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(exception);
				return Base64.getEncoder().encodeToString(baos.toByteArray());
			}
		}
	}

	public static Exception deserialize(String value) throws IOException, ClassNotFoundException {
		byte[] data = Base64.getDecoder().decode(value);
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
			return (Exception) ois.readObject();
		}
	}
}
