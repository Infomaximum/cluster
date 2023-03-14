package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.TestCluster;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.complex.RControllerComplex;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ComplexComponentTest {

    @Test
    public void testGet() {
        try (Cluster cluster = TestCluster.build()) {

            ManagerComponent managerComponent = cluster.getAnyLocalComponent(ManagerComponent.class);
            RControllerComplex rControllerComplex = managerComponent.getRemotes().get(Custom1Component.class, RControllerComplex.class);

            Assertions.assertEquals("1212", rControllerComplex.get("12", 12));

        }
    }

}
