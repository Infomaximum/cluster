package com.infomaximum.cluster.utils;

public class ChainBytesUtils {

    public static byte get(int index) {
        return (byte) (index % 256 - 128);
    }
}
