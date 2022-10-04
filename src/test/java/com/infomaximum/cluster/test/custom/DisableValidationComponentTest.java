package com.infomaximum.cluster.test.custom;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.server.custom.CustomComponent;
import com.infomaximum.cluster.server.custom.remote.disablevalidation.RControllerDisableValidation;
import com.infomaximum.cluster.test.BaseClusterTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 26.08.16.
 */
public class DisableValidationComponentTest extends BaseClusterTest {

    private final static Logger log = LoggerFactory.getLogger(DisableValidationComponentTest.class);

    @Test
    public void test() throws Exception {
        ManagerComponent managerComponent = getCluster().getAnyLocalComponent(ManagerComponent.class);
        RControllerDisableValidation rController = managerComponent.getRemotes().get(CustomComponent.class, RControllerDisableValidation.class);

        String value = "123";
        Assertions.assertEquals(value, rController.get(value));
    }

}
