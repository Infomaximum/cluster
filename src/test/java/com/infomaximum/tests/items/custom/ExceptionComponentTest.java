package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.TestCluster;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.exception.RControllerException;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.NotLinkException;

/**
 * Created by kris on 26.08.16.
 */
public class ExceptionComponentTest {

    private final static Logger log = LoggerFactory.getLogger(ExceptionComponentTest.class);

    @Test
    public void test() throws Exception {
        try (Cluster cluster = TestCluster.build()) {
            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            RControllerException rController = managerComponent.getRemotes().get(Custom1Component.class, RControllerException.class);

            try {
                rController.getException("123");
                Assertions.fail();
            } catch (Exception e) {
                Assertions.assertEquals(NotLinkException.class, e.getClass());
            }
        }
    }

}
