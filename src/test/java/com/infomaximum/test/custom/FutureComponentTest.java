package com.infomaximum.test.custom;

import com.infomaximum.test.BaseClusterTest;
import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.component.custom.remote.future.RControllerFuture;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.NotLinkException;
import java.util.concurrent.Future;

/**
 * Created by kris on 26.08.16.
 */
public class FutureComponentTest extends BaseClusterTest {

    private final static Logger log = LoggerFactory.getLogger(FutureComponentTest.class);

    @Test
    public void testGet() throws Exception {
        ManagerComponent managerComponent = getCluster().getAnyComponent(ManagerComponent.class);
        RControllerFuture rControllerFuture = managerComponent.getRemotes().get(CustomComponent.class, RControllerFuture.class);

        Future<String> future1 = rControllerFuture.get("123", 0);
        Assert.assertEquals("123", future1.get());

        Future<String> future2 = rControllerFuture.get("123", 100);
        Assert.assertEquals("123", future2.get());

    }

    @Test
    public void testErrorGet() throws Exception {
        ManagerComponent managerComponent = getCluster().getAnyComponent(ManagerComponent.class);
        RControllerFuture rControllerFuture = managerComponent.getRemotes().get(CustomComponent.class, RControllerFuture.class);

        Future<String> future1 = rControllerFuture.getError("123", 0);
        try {
            Assert.assertEquals("123", future1.get());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NotLinkException.class, e.getCause().getClass());
        }


        Future<String> future2 = rControllerFuture.getError("123", 100);
        try {
            Assert.assertEquals("123", future2.get());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NotLinkException.class, e.getCause().getClass());
        }
    }

}
