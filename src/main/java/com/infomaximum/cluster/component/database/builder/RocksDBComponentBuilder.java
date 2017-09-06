package com.infomaximum.cluster.component.database.builder;

import com.infomaximum.cluster.struct.Component;
import com.infomaximum.rocksdb.builder.RocksdbBuilder;

/**
 * Created by kris on 15.03.17.
 */
public class RocksDBComponentBuilder extends RocksdbBuilder {

    public final Component component;

    public RocksDBComponentBuilder(Component component) {
        this.component = component;
    }
}
