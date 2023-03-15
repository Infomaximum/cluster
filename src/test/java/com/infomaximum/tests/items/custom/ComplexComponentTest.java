package com.infomaximum.tests.items.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.complex.RControllerComplex;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ComplexComponentTest {

    @Test
    public void testGet() {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();

            ManagerComponent managerComponent = cluster1.getAnyLocalComponent(ManagerComponent.class);
            RControllerComplex rControllerComplex = managerComponent.getRemotes().get(Custom1Component.class, RControllerComplex.class);

            Assertions.assertEquals("1212", rControllerComplex.get("12", 12));

        }
    }

}
