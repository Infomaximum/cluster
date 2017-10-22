package com.infomaximum.test.custom;

import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.component.custom.remote.disablevalidation.RControllerDisableValidation;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.test.BaseClusterTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 26.08.16.
 */
public class DisableValidationComponentTest extends BaseClusterTest {

    private final static Logger log = LoggerFactory.getLogger(DisableValidationComponentTest.class);

    @Test
    public void test() throws Exception {
        ManagerComponent managerComponent = getCluster().getAnyComponent(ManagerComponent.class);
        RControllerDisableValidation rController = managerComponent.getRemotes().get(CustomComponent.class, RControllerDisableValidation.class);

        String value="123";
        Assert.assertEquals(value, rController.get(value));
    }

}
