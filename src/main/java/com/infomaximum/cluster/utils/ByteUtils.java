package com.infomaximum.cluster.utils;

public class ByteUtils {

    public static byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static boolean isNullOrEmpty(byte[] value) {
        return value == null || value.length == 0;
    }

    public static int writeInteger(byte[] target, int offset, int value) {
        target[offset] = (byte) (value >> 24);
        target[offset + 1] = (byte) (value >> 16);
        target[offset + 2] = (byte) (value >> 8);
        target[offset + 3] = (byte) (value /*>> 0*/);
        return offset + Integer.BYTES;
    }

    public static int getInteger(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 24) |
                ((bytes[offset + 1] & 0xFF) << 16) |
                ((bytes[offset + 2] & 0xFF) << 8) |
                ((bytes[offset + 3] & 0xFF) << 0);
    }

    public static int writeLong(byte[] target, int offset, long value) {
        target[offset] = (byte) (value >>> 56);
        target[offset + 1] = (byte) (value >>> 48);
        target[offset + 2] = (byte) (value >>> 40);
        target[offset + 3] = (byte) (value >>> 32);
        target[offset + 4] = (byte) (value >>> 24);
        target[offset + 5] = (byte) (value >>> 16);
        target[offset + 6] = (byte) (value >>> 8);
        target[offset + 7] = (byte) value;
        return offset + Long.BYTES;
    }

    public static long getLong(byte[] bytes, int offset) {
        return ((long) bytes[offset] << 56) +
                ((long) (bytes[offset + 1] & 255) << 48) +
                ((long) (bytes[offset + 2] & 255) << 40) +
                ((long) (bytes[offset + 3] & 255) << 32) +
                ((long) (bytes[offset + 4] & 255) << 24) +
                (long) ((bytes[offset + 5] & 255) << 16) +
                (long) ((bytes[offset + 6] & 255) << 8) +
                (long) ((bytes[offset + 7] & 255));
    }
}
