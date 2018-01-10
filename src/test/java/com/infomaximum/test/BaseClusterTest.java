package com.infomaximum.test;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.ComponentBuilder;
import com.infomaximum.cluster.builder.transport.MockTransportBuilder;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.custom.CustomComponent;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * Created by kris on 22.04.17.
 * integrationtest_subsystems@leeching.net
 */
public abstract class BaseClusterTest {

    private static Cluster cluster;

    @BeforeClass
    public static void init() throws Exception {
        cluster = new Cluster.Builder()
                .withTransport(
                        new MockTransportBuilder()
                )
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
                .withComponentIfNotExist(new ComponentBuilder(CustomComponent.class))
                .build();
    }

    public static Cluster getCluster() {
        return cluster;
    }

    @AfterClass
    public static void destroy() throws IOException {
        cluster.close();
    }
}
