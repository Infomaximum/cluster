package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.disablevalidation.RControllerDisableValidation;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.remote.Remotes;
import com.infomaximum.cluster.core.service.componentuuid.ComponentUuidManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerComponentTest {

    private final static Logger log = LoggerFactory.getLogger(DisableValidationComponentTest.class);

    @Test
    public void test() {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();

            ComponentUuidManager componentUuidManager = new ComponentUuidManager();

            Remotes remotes = cluster1.getAnyLocalComponent(ManagerComponent.class).getRemotes();
            Assertions.assertTrue(
                    remotes.isController(
                            componentUuidManager.getUuid(Custom1Component.class),
                            RControllerDisableValidation.class
                    )
            );
            Assertions.assertFalse(
                    remotes.isController(
                            ManagerComponent.UUID,
                            RControllerDisableValidation.class
                    )
            );
        }

    }

}
