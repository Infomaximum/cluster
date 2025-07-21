package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.future.RControllerFuture;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.memory.remote.RControllerMemory;
import com.infomaximum.cluster.core.remote.RemoteTarget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 26.08.16.
 */
public class AboutControllerTest {

    private final static Logger log = LoggerFactory.getLogger(AboutControllerTest.class);

    @Test
    public void test() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();

            ManagerComponent managerComponent1 = cluster1.getAnyLocalComponent(ManagerComponent.class);
            RControllerFuture rControllerFuture = managerComponent1.getRemotes().get(Custom1Component.class, RControllerFuture.class);

            Assertions.assertEquals(cluster1.node.getRuntimeId(), rControllerFuture.getNodeRuntimeId());
            Assertions.assertEquals("com.infomaximum.cluster.component.custom1", rControllerFuture.getComponentUuid());

            //Проверка RemoteTarget
            MemoryComponent memoryComponent1 = cluster1.getAnyLocalComponent(MemoryComponent.class);
            RControllerMemory rControllerMemory1 = managerComponent1.getRemotes().get(MemoryComponent.class, RControllerMemory.class);
            Assertions.assertEquals(new RemoteTarget(cluster1.node.getRuntimeId(), memoryComponent1.getId(), memoryComponent1.getInfo().getUuid()), rControllerMemory1.getRemoteTarget());
        }
    }


}
