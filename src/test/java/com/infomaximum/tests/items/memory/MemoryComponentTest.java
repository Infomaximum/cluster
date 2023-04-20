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
            for (int i = 0; i < 100; i++) {
                Cluster cluster1 = clusters.getCluster1();
                long t1 = System.currentTimeMillis();
                ManagerComponent managerComponent = cluster1.getAnyLocalComponent(ManagerComponent.class);
                long t2 = System.currentTimeMillis();
                RControllerMemory rControllerMemory = managerComponent.getRemotes().get(MemoryComponent.class, RControllerMemory.class);
                long t3 = System.currentTimeMillis();
                String key = "ping";
                String value = "pong";
                rControllerMemory.set(key, value);
                long t4 = System.currentTimeMillis();
                Assertions.assertEquals(value, rControllerMemory.get(key));
                long t5 = System.currentTimeMillis();
                System.out.println("t2-t1=" + (t2 - t1) + ", t3-t2=" + (t3 - t2) + ", t4-t3=" + (t4 - t3) + ", t5-t4=" + (t5 - t4) + "all=" + (t5 - t1));
                log.warn("t2-t1={}, t3-t2={}, t4-t3={}, t5-t4={}", t2 - t1, t3 - t2, t4 - t3, t5 - t4);
            }

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
