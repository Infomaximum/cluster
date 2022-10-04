package com.infomaximum.cluster.utils;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimitivesTest {

    @Test
    public void test() throws Exception {
        Assertions.assertEquals(boolean.class, Primitives.toPrimitive(Boolean.class));
        Assertions.assertEquals(byte.class, Primitives.toPrimitive(Byte.class));
        Assertions.assertEquals(char.class, Primitives.toPrimitive(Character.class));
        Assertions.assertEquals(double.class, Primitives.toPrimitive(Double.class));
        Assertions.assertEquals(float.class, Primitives.toPrimitive(Float.class));
        Assertions.assertEquals(int.class, Primitives.toPrimitive(Integer.class));
        Assertions.assertEquals(long.class, Primitives.toPrimitive(Long.class));
        Assertions.assertEquals(short.class, Primitives.toPrimitive(Short.class));
        Assertions.assertEquals(void.class, Primitives.toPrimitive(Void.class));

        Assertions.assertEquals(String.class, Primitives.toPrimitive(String.class));
    }
}
