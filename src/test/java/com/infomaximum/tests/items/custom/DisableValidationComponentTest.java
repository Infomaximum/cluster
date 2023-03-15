package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.disablevalidation.RControllerDisableValidation;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 26.08.16.
 */
public class DisableValidationComponentTest {

    private final static Logger log = LoggerFactory.getLogger(DisableValidationComponentTest.class);

    @Test
    public void test() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();

            ManagerComponent managerComponent = cluster1.getAnyLocalComponent(ManagerComponent.class);
            RControllerDisableValidation rController = managerComponent.getRemotes().get(Custom1Component.class, RControllerDisableValidation.class);

            String value = "123";
            Assertions.assertEquals(value, rController.get(value));
        }
    }

}
