package com.infomaximum.tests.items.packer;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.RControllerClusterInputStream;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.utils.ChainBytesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClusterInputStreamTest {

    private final static Logger log = LoggerFactory.getLogger(ClusterInputStreamTest.class);

    @Test
    public void test() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();
            Cluster cluster2 = clusters.getCluster2();

            ManagerComponent managerComponent = cluster1.getAnyLocalComponent(ManagerComponent.class);

            RControllerClusterInputStream controllerClusterInputStream = managerComponent.getRemotes().getFromCKey(
                    cluster2.node.getRuntimeId(),
                    cluster2.getAnyLocalComponent(Custom1Component.class).getId(),
                    RControllerClusterInputStream.class
            );

            check(controllerClusterInputStream, 0);
            check(controllerClusterInputStream, 10);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE - 1);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE + 1);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE + 10);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE * 2);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE * 2 - 2);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE * 2 - 1);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE * 3 + 1);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE * 5);
            check(controllerClusterInputStream, ClusterInputStream.BATCH_SIZE * 5 - 1);
        }
    }

    private void check(RControllerClusterInputStream controllerClusterInputStream, int size) throws IOException {
        ClusterInputStream clusterInputStream = controllerClusterInputStream.getInputStream(size);
        byte[] bytes = clusterInputStream.readAllBytes();
        Assertions.assertEquals(size, bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            Assertions.assertEquals(bytes[i], ChainBytesUtils.get(i));
        }
    }
}
