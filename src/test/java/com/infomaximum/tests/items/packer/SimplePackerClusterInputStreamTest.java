package com.infomaximum.tests.items.packer;

import com.infomaximum.cluster.core.remote.packer.impl.RemotePackerClusterInputStream;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class SimplePackerClusterInputStreamTest {

    private final Random random = new Random();
    ;

    @Test
    public void test() throws Exception {
        check(0, 0, ClusterInputStream.BATCH_SIZE, ClusterInputStream.BATCH_SIZE);
        check(0, 0, ClusterInputStream.BATCH_SIZE, 0);
        for (int i = 0; i < 1000; i++) {
            int batchSize = random.nextInt(1, ClusterInputStream.BATCH_SIZE * 10);
            check(random.nextInt(), random.nextInt(0, Integer.MAX_VALUE), batchSize, random.nextInt(0, batchSize));
        }
    }

    private void check(int sourceComponentUniqueId, int id, int batchSize, int dataLength) {
        byte[] sourceData = new byte[dataLength];
        random.nextBytes(sourceData);
        byte[] proxy = new RemotePackerClusterInputStream.Packer(sourceComponentUniqueId, id, batchSize, sourceData, 0, dataLength).serialize();

        RemotePackerClusterInputStream.Packer restorePacker = RemotePackerClusterInputStream.Packer.deserialize(proxy);
        byte[] restoreData = new byte[restorePacker.dataLength];
        System.arraycopy(restorePacker.data, restorePacker.dataOffset, restoreData, 0, restorePacker.dataLength);

        Assertions.assertEquals(sourceComponentUniqueId, restorePacker.sourceComponentUniqueId);
        Assertions.assertEquals(id, restorePacker.id);
        Assertions.assertEquals(batchSize, restorePacker.batchSize);
        Assertions.assertArrayEquals(sourceData, restoreData);
    }
}
