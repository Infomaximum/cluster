package com.infomaximum.cluster.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kris on 18.01.17.
 */
public class CacheClassForName {

    private static Map<String, Class> cache = new HashMap<String, Class>();

    public static Class get(String className) throws ClassNotFoundException {
        Class clazz = cache.get(className);
        if (clazz!=null) return clazz;
        synchronized (cache) {
            clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
            cache.put(className, clazz);
        }
        return clazz;
    }

}
