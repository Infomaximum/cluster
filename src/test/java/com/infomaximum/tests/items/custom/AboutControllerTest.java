package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.future.RControllerFuture;
import com.infomaximum.cluster.component.manager.ManagerComponent;
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

            ManagerComponent managerComponent = cluster1.getAnyLocalComponent(ManagerComponent.class);
            RControllerFuture rControllerFuture = managerComponent.getRemotes().get(Custom1Component.class, RControllerFuture.class);

            Assertions.assertEquals(1, rControllerFuture.getNode());
            Assertions.assertEquals("com.infomaximum.cluster.component.custom1", rControllerFuture.getComponentUuid());
        }
    }


}
