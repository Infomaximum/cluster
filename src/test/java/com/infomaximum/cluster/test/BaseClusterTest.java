package com.infomaximum.cluster.test;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.ComponentBuilder;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.server.custom.CustomComponent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

/**
 * Created by kris on 22.04.17.
 * integrationtest_subsystems@leeching.core
 */
public abstract class BaseClusterTest {

    private static Cluster cluster;

    @BeforeAll
    public static void init() {
        cluster = new Cluster.Builder()
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .build();
    }

    public static Cluster getCluster() {
        return cluster;
    }

    @AfterAll
    public static void destroy() throws IOException {
        cluster.close();
    }
}
