package com.infomaximum.cluster.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ReflectionUtils {

    public static Class getRawClass(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        } else if (type instanceof TypeVariable) {
//            for (Type iiType : ((TypeVariable) type).getBounds()) {
//                validation(iiType);
//            }
            //TODO не вытаскиваем raw class
            throw new RuntimeException("not implement get raw class");
        } else {
            return (Class) type;
        }
    }
}
