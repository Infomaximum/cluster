package com.infomaximum.tests.items.memory;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.memory.remote.RControllerMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by kris on 26.08.16.
 */
public class MemoryComponentTest {

    private final static Logger log = LoggerFactory.getLogger(MemoryComponentTest.class);

    @Test
    public void test1() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();

            ManagerComponent managerComponent = cluster1.getAnyLocalComponent(ManagerComponent.class);
            RControllerMemory rControllerMemory = managerComponent.getRemotes().get(MemoryComponent.class, RControllerMemory.class);

            String key = "ping";
            String value = "pong";

            rControllerMemory.set(key, value);

            Assertions.assertEquals(value, rControllerMemory.get(key));
        }
    }

    @Test
    public void test2() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();

            ManagerComponent managerComponent = cluster1.getAnyLocalComponent(ManagerComponent.class);
            RControllerMemory rControllerMemory = managerComponent.getRemotes().get(MemoryComponent.class, RControllerMemory.class);

            HashMap<String, Serializable> values = new HashMap<>();
            values.put("key1", "value1");
            values.put("key2", "value2");
            rControllerMemory.sets(values);

            Assertions.assertEquals("value1", rControllerMemory.get("key1"));
            Assertions.assertEquals("value2", rControllerMemory.get("key2"));
        }
    }

}
