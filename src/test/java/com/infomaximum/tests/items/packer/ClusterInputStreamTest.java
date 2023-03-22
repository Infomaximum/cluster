package com.infomaximum.tests.items.packer;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.remote.RControllerClusterInputStream;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class ClusterInputStreamTest {

    private final static Logger log = LoggerFactory.getLogger(ClusterInputStreamTest.class);

    @Test
    public void test1() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();
            Cluster cluster2 = clusters.getCluster2();

            ManagerComponent managerComponent = cluster1.getAnyLocalComponent(ManagerComponent.class);

            Collection<RControllerClusterInputStream> controllerClusterInputStreams = managerComponent.getRemotes().getControllers(RControllerClusterInputStream.class);
            Assertions.assertEquals(2, controllerClusterInputStreams.size());//Кластеры не видят друг друга - необходимо сначала решить эту проблему - потом двигаться дальше
            System.out.println("");

        }
    }
}
