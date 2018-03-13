package com.infomaximum.cluster.utils;

public class ByteUtils {

    public static byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static boolean isNullOrEmpty(byte[] value) {
        return value == null || value.length == 0;
    }

}
