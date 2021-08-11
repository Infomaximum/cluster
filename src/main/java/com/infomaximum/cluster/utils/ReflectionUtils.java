package com.infomaximum.cluster.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ReflectionUtils {

    public static Class getRawClass(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        } else if (type instanceof TypeVariable) {
            return (Class) (((TypeVariable) type).getGenericDeclaration());
        } else {
            return (Class) type;
        }
    }
}
