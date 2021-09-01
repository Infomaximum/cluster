package com.infomaximum.cluster.utils;

import org.junit.Assert;
import org.junit.Test;

public class GlobalUniqueIdUtilsTest {

    @Test
    public void test() throws Exception {
        for (byte node = 0; node < Byte.MAX_VALUE; node++) {
            for (int localId = 0; localId < 8388607; localId++) {
                int globalUniqueId = GlobalUniqueIdUtils.getGlobalUniqueId(node, localId);

                Assert.assertEquals(node, GlobalUniqueIdUtils.getNode(globalUniqueId));
                Assert.assertEquals(localId, GlobalUniqueIdUtils.getLocalId(globalUniqueId));

//                System.out.println(globalUniqueId);
            }
        }
    }
}
