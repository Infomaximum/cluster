package com.infomaximum.remoteobject.test;

import com.infomaximum.cluster.core.remote.packer.RemotePackerRemoteObject;
import com.infomaximum.remoteobject.struct.valide.RemoteObject1;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class RemoteObject1Test {

    private final static Logger log = LoggerFactory.getLogger(RemoteObject1Test.class);

    @Test
    public void test1() throws Exception {
        RemotePackerRemoteObject remotePackerRemoteObject = new RemotePackerRemoteObject();

        RemoteObject1 remoteObject11 = new RemoteObject1(1, 6L);

        byte[] bytes = remotePackerRemoteObject.serialize(null, remoteObject11);

        RemoteObject1 remoteObject12 = (RemoteObject1) remotePackerRemoteObject.deserialize(null, null, bytes);

        Assert.assertEquals(remoteObject11.getK(), remoteObject12.getK());
        Assert.assertEquals(remoteObject11.getL1(), remoteObject12.getL1());
    }
}
