package com.infomaximum.cluster.component.database.utils;

/**
 * Created by user on 25.08.2017.
 */
public class DatabaseComponentUtil {

    //TODO разобраться с uuid
    public static String getKey(int shard) {
        return new StringBuilder()
                .append("com.infomaximum.subsystem.database").append(':').append(shard)
                .toString();
    }

}
