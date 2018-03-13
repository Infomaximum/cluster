package com.infomaximum.cluster.component.memory.struct;

/**
 * Created by kris on 09.03.16.
 */
public interface MemoryPackerItem {

    Object deserialize(final String strValue);

    String serialize(final Object value);
}
