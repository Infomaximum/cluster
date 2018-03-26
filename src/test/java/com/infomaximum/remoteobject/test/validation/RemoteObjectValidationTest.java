package com.infomaximum.remoteobject.test.validation;

import com.infomaximum.cluster.core.remote.packer.RemotePackerRemoteObject;
import com.infomaximum.cluster.core.remote.utils.validatorremoteobject.RemoteObjectValidator;
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
        RemoteObjectValidator.validation(RemoteObject1.class).check();
    }

    @Test
    public void test2() {
        RemoteObjectValidator.validation(RemoteObject2.class).check();
    }

    @Test
    public void test3() {
        RemoteObjectValidator.validation(RemoteObject3.class).check();
    }

    @Test
    public void testFail1() {
        try {
            RemoteObjectValidator.validation(RemoteObjectFail1.class).check();
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testFail2() {
        try {
            RemoteObjectValidator.validation(RemoteObjectFail2.class).check();
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testFail3() {
        try {
            RemoteObjectValidator.validation(RemoteObjectFail3.class).check();
            Assert.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testFail4() {
        try {
            RemoteObjectValidator.validation(RemoteObjectFail4.class).check();
            Assert.fail();
        } catch (Exception e) {
        }
    }
}
