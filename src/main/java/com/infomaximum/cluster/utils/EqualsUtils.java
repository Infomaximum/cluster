package com.infomaximum.cluster.utils;

/**
 * Created by kris on 29.05.17.
 */
public class EqualsUtils {

    public static boolean equals(Object value1, Object value2) {
        if (value1 == null) {
            return (value2 == null);
        } else {
            return value1.equals(value2);
        }
    }

    public static boolean equalsType(Class class1, Class class2) {
        if (class1 == class2) return true;
        if (class1.isPrimitive() && !class2.isPrimitive()) {
            return (class1 == Primitives.toPrimitive(class2));
        } else if (!class1.isPrimitive() && class2.isPrimitive()) {
            return (Primitives.toPrimitive(class1) == class2);
        }
        return false;
    }
}
