package com.infomaximum.cluster.component.future;

import com.infomaximum.cluster.ClusterTest;
import com.infomaximum.cluster.component.future.remote.RControllerFuture;
import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.memory.remote.RControllerMemory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

/**
 * Created by kris on 26.08.16.
 */
public class FutureComponentTest extends ClusterTest {

    private final static Logger log = LoggerFactory.getLogger(FutureComponentTest.class);

    @Test
    public void test() throws Exception {
        ManagerComponent managerComponent = getCluster().getLoaderComponents().getAnyComponent(ManagerComponent.class);
        RControllerFuture rControllerFuture = managerComponent.getRemotes().get(FutureComponent.class, RControllerFuture.class);

        Future<String> future = rControllerFuture.get("123");

    }

}
