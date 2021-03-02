package com.infomaximum.cluster.test.custom;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.remote.Remotes;
import com.infomaximum.cluster.server.custom.CustomComponent;
import com.infomaximum.cluster.server.custom.remote.disablevalidation.RControllerDisableValidation;
import com.infomaximum.cluster.test.BaseClusterTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerComponentTest extends BaseClusterTest {

    private final static Logger log = LoggerFactory.getLogger(DisableValidationComponentTest.class);

    @Test
    public void test() {
        Remotes remotes = getCluster().getAnyComponent(ManagerComponent.class).getRemotes();
        Assert.assertTrue(
                remotes.isController(
                        CustomComponent.INFO.getUuid(),
                        RControllerDisableValidation.class
                )
        );
        Assert.assertFalse(
                remotes.isController(
                        ManagerComponent.INFO.getUuid(),
                        RControllerDisableValidation.class
                )
        );
    }

}
