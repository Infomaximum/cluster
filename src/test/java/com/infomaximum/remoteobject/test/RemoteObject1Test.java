package com.infomaximum.remoteobject.test;

import com.infomaximum.cluster.core.remote.packer.RemotePackerRemoteObject;
import com.infomaximum.remoteobject.struct.valide.RemoteObject1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteObject1Test {

    private final static Logger log = LoggerFactory.getLogger(RemoteObject1Test.class);

    @Test
    public void test1() throws Exception {
        RemotePackerRemoteObject remotePackerRemoteObject = new RemotePackerRemoteObject();

        RemoteObject1 remoteObject11 = new RemoteObject1(1, 6L);

        byte[] bytes = remotePackerRemoteObject.serialize(null, remoteObject11);

        RemoteObject1 remoteObject12 = (RemoteObject1) remotePackerRemoteObject.deserialize(null, null, bytes);

        Assertions.assertEquals(remoteObject11.getK(), remoteObject12.getK());
        Assertions.assertEquals(remoteObject11.getL1(), remoteObject12.getL1());
    }
}
