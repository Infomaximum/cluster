package com.infomaximum.cluster.test.custom;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.server.custom.CustomComponent;
import com.infomaximum.cluster.server.custom.remote.complex.RControllerComplex;
import com.infomaximum.cluster.test.BaseClusterTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ComplexComponentTest extends BaseClusterTest {

    @Test
    public void testGet() {
        ManagerComponent managerComponent = getCluster().getAnyLocalComponent(ManagerComponent.class);
        RControllerComplex rControllerComplex = managerComponent.getRemotes().get(CustomComponent.class, RControllerComplex.class);

        Assertions.assertEquals("1212", rControllerComplex.get("12", 12));
    }

}
