package com.infomaximum.cluster.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Primitives {

    private Primitives() {
    }

    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;

    static {
        Map<Class<?>, Class<?>> wrapToPrim = new LinkedHashMap<>(16);
        wrapToPrim.put(Boolean.class, boolean.class);
        wrapToPrim.put(Byte.class, byte.class);
        wrapToPrim.put(Character.class, char.class);
        wrapToPrim.put(Double.class, double.class);
        wrapToPrim.put(Float.class, float.class);
        wrapToPrim.put(Integer.class, int.class);
        wrapToPrim.put(Long.class, long.class);
        wrapToPrim.put(Short.class, short.class);
        wrapToPrim.put(Void.class, void.class);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
    }

    public static <T> Class<T> toPrimitive(Class<T> type) {
        Class<T> unwrapped = (Class<T>) WRAPPER_TO_PRIMITIVE_TYPE.get(type);
        return (unwrapped == null) ? type : unwrapped;
    }
}
