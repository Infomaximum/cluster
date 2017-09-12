package com.infomaximum.cluster;

import com.infomaximum.cluster.builder.ClusterBuilder;
import com.infomaximum.cluster.builder.component.ComponentBuilder;
import com.infomaximum.cluster.builder.transport.MockTransportBuilder;
import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.component.future.FutureComponent;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by kris on 22.04.17.
 * integrationtest_subsystems@leeching.net
 */
public class ClusterTest {

    private static Path pathDataBase;
    private static Cluster cluster;

    @BeforeClass
    public static void init() throws IOException, ReflectiveOperationException {
        pathDataBase = Files.createTempDirectory("rocksdb").toAbsolutePath();
        pathDataBase.toFile().deleteOnExit();

        cluster = new ClusterBuilder()
                .withTransport(
                        new MockTransportBuilder()
                )
                .withComponentIfNotExist(new ComponentBuilder(MemoryComponent.class))
//                .withComponentIfNotExist(new ComponentBuilder(FutureComponent.class))
                .build();
    }

    public static Cluster getCluster() {
        return cluster;
    }

    @AfterClass
    public static void destroy() throws IOException {
        cluster.destroy();
        FileUtils.deleteDirectory(pathDataBase.toAbsolutePath().toFile());
    }
}
