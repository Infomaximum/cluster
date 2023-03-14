package com.infomaximum.cluster;

import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.exception.ClusterException;

public class TestCluster {

    private TestCluster() {
    }

    public static Cluster build() throws ClusterException {
        return new Cluster.Builder()
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .withComponentIfNotExist(new ComponentBuilder(Custom1Component.class))
                .build();
    }

}
