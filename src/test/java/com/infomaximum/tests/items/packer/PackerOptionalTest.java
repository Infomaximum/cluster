package com.infomaximum.tests.items.packer;

import com.infomaximum.cluster.core.remote.packer.impl.RemotePackerOptional;
import com.infomaximum.cluster.remoteobject.struct.valide.RemoteObject1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PackerOptionalTest {

    private final static Logger log = LoggerFactory.getLogger(PackerOptionalTest.class);

    @Test
    public void testOptionalRemoteObject() {
        RemotePackerOptional remotePackerOptional = new RemotePackerOptional();

        RemoteObject1 remoteObject11Value = new RemoteObject1(1, 6L);
        Optional<RemoteObject1> remoteObject11 = Optional.ofNullable(remoteObject11Value);

        byte[] bytes = remotePackerOptional.serialize(null, remoteObject11);

        Optional remoteObject12 = remotePackerOptional.deserialize(null, null, bytes);
        RemoteObject1 remoteObject12Value = (RemoteObject1) remoteObject12.orElse(null);

        Assertions.assertNotNull(remoteObject12Value);
        Assertions.assertEquals(remoteObject11Value.getK(), remoteObject12Value.getK());
        Assertions.assertEquals(remoteObject11Value.getL1(), remoteObject12Value.getL1());
    }

    @Test
    public void testOptionalNull() {
        RemotePackerOptional remotePackerOptional = new RemotePackerOptional();

        Optional<RemoteObject1> remoteObject11 = Optional.ofNullable(null);

        byte[] bytes = remotePackerOptional.serialize(null, remoteObject11);

        Optional remoteObject12 = remotePackerOptional.deserialize(null, null, bytes);
        RemoteObject1 remoteObject12Value = (RemoteObject1) remoteObject12.orElse(null);

        Assertions.assertNull(remoteObject12Value);
    }
}
