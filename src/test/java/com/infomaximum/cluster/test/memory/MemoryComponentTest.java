package com.infomaximum.cluster.test.memory;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.memory.remote.RControllerMemory;
import com.infomaximum.cluster.test.BaseClusterTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by kris on 26.08.16.
 */
public class MemoryComponentTest extends BaseClusterTest {

    private final static Logger log = LoggerFactory.getLogger(MemoryComponentTest.class);

    @Test
    public void test1() throws Exception {
        ManagerComponent managerComponent = getCluster().getAnyComponent(ManagerComponent.class);
        RControllerMemory rControllerMemory = managerComponent.getRemotes().get(MemoryComponent.INFO.getUuid(), RControllerMemory.class);

        String key = "ping";
        String value = "pong";

        rControllerMemory.set(key, value);

        Assert.assertEquals(value, rControllerMemory.get(key));
    }

    @Test
    public void test() throws Exception {
        ManagerComponent managerComponent = getCluster().getAnyComponent(ManagerComponent.class);
        RControllerMemory rControllerMemory = managerComponent.getRemotes().get(MemoryComponent.INFO.getUuid(), RControllerMemory.class);

        rControllerMemory.sets(new HashMap<String, Serializable>() {{
            put("key1", "value1");
            put("key2", "value2");
        }});

        Assert.assertEquals("value1", rControllerMemory.get("key1"));
        Assert.assertEquals("value2", rControllerMemory.get("key2"));
    }

}
