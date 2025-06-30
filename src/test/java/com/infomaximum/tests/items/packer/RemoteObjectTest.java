package com.infomaximum.tests.items.packer;

import com.infomaximum.cluster.Clusters;
import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.core.remote.packer.impl.RemotePackerRemoteObject;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.remoteobject.struct.valide.RemoteObject1;
import com.infomaximum.cluster.remoteobject.struct.valide.RemoteObjectWithClusterInputStream;
import com.infomaximum.cluster.utils.ByteUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Stream;

public class RemoteObjectTest {

    private final static Logger log = LoggerFactory.getLogger(RemoteObjectTest.class);

    @Test
    public void test1() {
        RemotePackerRemoteObject remotePackerRemoteObject = new RemotePackerRemoteObject();

        RemoteObject1 remoteObject11 = new RemoteObject1(1, 6L);

        byte[] bytes = remotePackerRemoteObject.serialize(null, remoteObject11);

        RemoteObject1 remoteObject12 = (RemoteObject1) remotePackerRemoteObject.deserialize(null, null, bytes);

        Assertions.assertEquals(remoteObject11.getK(), remoteObject12.getK());
        Assertions.assertEquals(remoteObject11.getL1(), remoteObject12.getL1());
    }

    @ParameterizedTest
    @MethodSource("getByteArrays")
    public void test2(byte[] array) throws IOException {
        try (Clusters clusters = new Clusters.Builder().build()) {
            Custom1Component component = clusters.getCluster1().getAnyLocalComponent(Custom1Component.class);
            RemotePackerRemoteObject remotePackerRemoteObject = new RemotePackerRemoteObject();
            RemoteObjectWithClusterInputStream remoteObject1 = new RemoteObjectWithClusterInputStream(new ClusterInputStream(new ByteArrayInputStream(array)));

            byte[] bytes = remotePackerRemoteObject.serialize(component, remoteObject1);
            RemoteObjectWithClusterInputStream remoteObject2 = (RemoteObjectWithClusterInputStream) remotePackerRemoteObject.deserialize(component, null, bytes);
            try (ClusterInputStream clusterInputStream2 = remoteObject2.clusterInputStream()) {
                byte[] res2 = clusterInputStream2.readAllBytes();
                Assertions.assertArrayEquals(array, res2);
            }
        }
    }

    private static Stream<Arguments> getByteArrays() {
        return Stream.of(
                Arguments.of((Object) new byte[]{1, 23, 4, 5, 67, 78}),
                Arguments.of((Object) new byte[5 * ClusterInputStream.BATCH_SIZE]),
                Arguments.of((Object) ByteUtils.EMPTY_BYTE_ARRAY)
        );
    }
}
