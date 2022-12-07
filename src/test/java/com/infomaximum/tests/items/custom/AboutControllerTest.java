package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.TestCluster;
import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.component.custom.remote.future.RControllerFuture;
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
        try (Cluster cluster = TestCluster.build()) {
            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            RControllerFuture rControllerFuture = managerComponent.getRemotes().get(CustomComponent.class, RControllerFuture.class);

            Assertions.assertEquals(0, rControllerFuture.getNode());
            Assertions.assertEquals("com.infomaximum.cluster.component.custom", rControllerFuture.getComponentUuid());
        }
    }


}
