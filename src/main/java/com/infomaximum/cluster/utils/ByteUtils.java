package com.infomaximum.cluster.utils;

public class ByteUtils {

    public static byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static boolean isNullOrEmpty(byte[] value) {
        return value == null || value.length == 0;
    }

    public static void writeInteger(byte[] target, int offset, int value) {
        target[offset] = (byte) (value >> 24);
        target[offset + 1] = (byte) (value >> 16);
        target[offset + 2] = (byte) (value >> 8);
        target[offset + 3] = (byte) (value /*>> 0*/);
    }

    public static int getInteger(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 24) |
                ((bytes[offset + 1] & 0xFF) << 16) |
                ((bytes[offset + 2] & 0xFF) << 8) |
                ((bytes[offset + 3] & 0xFF) << 0);
    }
}
