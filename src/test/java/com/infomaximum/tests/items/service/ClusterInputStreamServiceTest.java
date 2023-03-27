package com.infomaximum.tests.items.service;

import com.infomaximum.cluster.component.service.internal.service.ClusterInputStreamService;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.exception.ClusterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class ClusterInputStreamServiceTest {

    @Test
    public void testExpire() throws Exception {
        ClusterInputStreamService clusterInputStreamService = new ClusterInputStreamService(Duration.ZERO);

        int id1 = clusterInputStreamService.register(new ClusterInputStream(new ByteArrayInputStream(new byte[0])));
        Thread.sleep(10);
        int id2 = clusterInputStreamService.register(new ClusterInputStream(new ByteArrayInputStream(new byte[0])));

        Assertions.assertThrows(ClusterException.class, () -> {
            clusterInputStreamService.read(id1, 1);
        });
    }
}
