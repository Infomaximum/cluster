package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.TestCluster;
import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.component.custom.remote.doublebraceinitialization.RDoubleBraceInitialization;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.exception.ClusterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class FailDoubleBraceInitializationTest {

    @Test
    public void test1() throws Exception {
        try (Cluster cluster = TestCluster.build()) {
            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            RDoubleBraceInitialization rDoubleBraceInitialization = managerComponent.getRemotes().get(CustomComponent.class, RDoubleBraceInitialization.class);

            Assertions.assertThrows(ClusterException.class, () -> {
                rDoubleBraceInitialization.failArgDoubleBraceInitialization(new HashMap<>() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }});
            });
        }
    }

    @Test
    public void test2() throws Exception {
        try (Cluster cluster = TestCluster.build()) {
            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            RDoubleBraceInitialization rDoubleBraceInitialization = managerComponent.getRemotes().get(CustomComponent.class, RDoubleBraceInitialization.class);

            Assertions.assertThrows(ClusterException.class, () -> {
                rDoubleBraceInitialization.failResultDoubleBraceInitialization();
            });
        }
    }
}
