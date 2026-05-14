package com.infomaximum.tests.items.packer;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.core.remote.packer.impl.RemotePackerClusterInputStream;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.exception.ClusterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Проверяет, что при отсутствии удалённой ноды или компонента
 * {@link RemotePackerClusterInputStream#getInputStream} бросает исключение,
 * сконструированное через {@code Cluster.getExceptionBuilder()},
 * а не {@code new ClusterRemotePackerException()} напрямую (PT-16407).
 */
public class RemotePackerClusterInputStreamThrowTest {

    /**
     * Несуществующая нода в Packer'е — {@link RemotePackerClusterInputStream#getInputStream}
     * бросает исключение, построенное дефолтным {@code ExceptionBuilderImpl}.
     * Это {@link ClusterException} с сообщением, идентифицирующим конкретные node/component id —
     * то есть путь проходит через билдер, а не через no-arg {@code new ClusterRemotePackerException()}.
     */
    @Test
    public void unknownRemoteNode_throwsViaExceptionBuilder() throws Exception {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Cluster cluster1 = clusters.getCluster1();
            Cluster cluster2 = clusters.getCluster2();
            Custom1Component component = cluster1.getAnyLocalComponent(Custom1Component.class);

            // nodeRuntimeId — существующий (cluster2), componentId — заведомо несуществующий:
            // это попадает в ветку runtimeComponentInfo == null именно по componentId,
            // моделируя ситуацию «компонент на удалённой ноде уже снят / нода ещё не зарегистрировала его».
            UUID knownNodeRuntimeId = cluster2.node.getRuntimeId();
            int unknownComponentId = 999_999;
            byte[] dummyData = new byte[ClusterInputStream.BATCH_SIZE];
            int validId = 42;

            RemotePackerClusterInputStream.Packer packer = new RemotePackerClusterInputStream.Packer(
                    knownNodeRuntimeId, unknownComponentId, validId, ClusterInputStream.BATCH_SIZE,
                    dummyData, 0, dummyData.length
            );

            ClusterException thrown = Assertions.assertThrows(
                    ClusterException.class,
                    () -> RemotePackerClusterInputStream.getInputStream(component, packer)
            );
            Assertions.assertNotNull(thrown.getMessage(),
                    "Билдер должен заполнить сообщение, в отличие от прежнего new ClusterRemotePackerException()");
            Assertions.assertTrue(thrown.getMessage().contains(knownNodeRuntimeId.toString()),
                    "Сообщение должно содержать sourceNodeRuntimeId: " + thrown.getMessage());
            Assertions.assertTrue(thrown.getMessage().contains(String.valueOf(unknownComponentId)),
                    "Сообщение должно содержать sourceComponentId: " + thrown.getMessage());
        }
    }
}
