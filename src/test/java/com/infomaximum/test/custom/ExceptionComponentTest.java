package com.infomaximum.test.custom;

import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.component.custom.remote.exception.RControllerException;
import com.infomaximum.cluster.component.custom.remote.future.RControllerFuture;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.test.ClusterTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.NotLinkException;
import java.util.concurrent.Future;

/**
 * Created by kris on 26.08.16.
 */
public class ExceptionComponentTest extends ClusterTest {

    private final static Logger log = LoggerFactory.getLogger(ExceptionComponentTest.class);

    @Test
    public void test() throws Exception {
        ManagerComponent managerComponent = getCluster().getLoaderComponents().getAnyComponent(ManagerComponent.class);
        RControllerException rController = managerComponent.getRemotes().get(CustomComponent.class, RControllerException.class);

        try {
            rController.getException("123");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NotLinkException.class, e.getClass());
        }
    }

}
