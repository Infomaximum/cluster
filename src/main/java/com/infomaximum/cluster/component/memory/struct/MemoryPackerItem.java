package com.infomaximum.cluster.component.memory.struct;

/**
 * Created by kris on 09.03.16.
 */
public interface MemoryPackerItem<T> {

    T deserialize(final byte[] value);

    byte[] serialize(final T value);
}
