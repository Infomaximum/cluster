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

import java.nio.file.NotLinkException;
import java.util.concurrent.Future;

/**
 * Created by kris on 26.08.16.
 */
public class FutureComponentTest {

    private final static Logger log = LoggerFactory.getLogger(FutureComponentTest.class);

    @Test
    public void testGet() throws Exception {
        try (Cluster cluster = TestCluster.build()) {
            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            RControllerFuture rControllerFuture = managerComponent.getRemotes().get(CustomComponent.class, RControllerFuture.class);

            Future<String> future1 = rControllerFuture.get("123", 0);
            Assertions.assertEquals("123", future1.get());

            Future<String> future2 = rControllerFuture.get("123", 100);
            Assertions.assertEquals("123", future2.get());
        }
    }

    @Test
    public void testErrorGet() throws Exception {
        try (Cluster cluster = TestCluster.build()) {
            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            RControllerFuture rControllerFuture = managerComponent.getRemotes().get(CustomComponent.class, RControllerFuture.class);

            Future<String> future1 = rControllerFuture.getError("123", 0);
            try {
                Assertions.assertEquals("123", future1.get());
                Assertions.fail();
            } catch (Exception e) {
                Assertions.assertEquals(NotLinkException.class, e.getCause().getClass());
            }


            Future<String> future2 = rControllerFuture.getError("123", 100);
            try {
                Assertions.assertEquals("123", future2.get());
                Assertions.fail();
            } catch (Exception e) {
                Assertions.assertEquals(NotLinkException.class, e.getCause().getClass());
            }
        }
    }

}
