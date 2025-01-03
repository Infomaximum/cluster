package com.infomaximum.tests.items.packer;

import com.infomaximum.cluster.core.remote.packer.impl.RemotePackerRemoteObject;
import com.infomaximum.cluster.remoteobject.struct.valide.RemoteObject1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
