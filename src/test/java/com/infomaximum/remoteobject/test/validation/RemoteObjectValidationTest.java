package com.infomaximum.remoteobject.test.validation;

import com.infomaximum.cluster.core.remote.packer.RemotePackerRemoteObject;
import com.infomaximum.remoteobject.struct.fail.RemoteObjectFail1;
import com.infomaximum.remoteobject.struct.fail.RemoteObjectFail2;
import com.infomaximum.remoteobject.struct.fail.RemoteObjectFail3;
import com.infomaximum.remoteobject.struct.fail.RemoteObjectFail4;
import com.infomaximum.remoteobject.struct.valide.RemoteObject1;
import com.infomaximum.remoteobject.struct.valide.RemoteObject2;
import com.infomaximum.remoteobject.struct.valide.RemoteObject3;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteObjectValidationTest {

    private final static Logger log = LoggerFactory.getLogger(RemoteObjectValidationTest.class);

    @Test
    public void test1() {
        RemotePackerRemoteObject.RemoteObjectValidator.validation(RemoteObject1.class);
    }

    @Test
    public void test2() {
        RemotePackerRemoteObject.RemoteObjectValidator.validation(RemoteObject2.class);
    }

    @Test
    public void test3() {
        RemotePackerRemoteObject.RemoteObjectValidator.validation(RemoteObject3.class);
    }

    @Test
    public void testFail1() {
        try {
            RemotePackerRemoteObject.RemoteObjectValidator.validation(RemoteObjectFail1.class);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testFail2() {
        try {
            RemotePackerRemoteObject.RemoteObjectValidator.validation(RemoteObjectFail2.class);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testFail3() {
        try {
            RemotePackerRemoteObject.RemoteObjectValidator.validation(RemoteObjectFail3.class);
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testFail4() {
        try {
            RemotePackerRemoteObject.RemoteObjectValidator.validation(RemoteObjectFail4.class);
            Assert.fail();
        } catch (Exception e) {
        }
    }
}
