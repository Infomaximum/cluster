package com.infomaximum.cluster.component.memory.struct;

import net.minidev.json.parser.ParseException;

/**
 * Created by kris on 09.03.16.
 */
public interface MemoryPackerItem {

    Object deserialize(final String strValue) throws ParseException;

    String serialize(final Object value);
}
