package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.packer.RemotePackerObject;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.Type;

/**
 * Created by user on 06.09.2017.
 */
public class ComponentRemotePacker {
    private final Component component;
    private final RemotePackerObject remotePackers;
    private final Class<?> expectedExceptionType;

    public ComponentRemotePacker(Remotes remotes) {
        this.component = remotes.component;
        this.remotePackers = component.getTransport().getRemotePackerObject();
        this.expectedExceptionType = remotes.cluster.getExceptionBuilder().getTypeException();
    }

    public byte[] serialize(Class classType, Object value) {
        return remotePackers.serialize(component, classType, value);
    }

    public byte[] serialize(Class classType, Object value, Thread.UncaughtExceptionHandler caughtExceptionHandler) throws Exception {
        try {
            return serialize(classType, value);
        } catch (Throwable e) {
            Throwable expected = matchExpected(e);
            if (expected != null) {
                throw (Exception) expected;
            }
            caughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
            return null;
        }
    }

    public Object deserialize(Class classType, byte[] value) throws Exception {
        return remotePackers.deserialize(component, classType, value);
    }

    public Object deserialize(Class classType, byte[] value, Thread.UncaughtExceptionHandler caughtExceptionHandler) throws Exception {
        try {
            return deserialize(classType, value);
        } catch (Throwable e) {
            Throwable expected = matchExpected(e);
            if (expected != null) {
                throw (Exception) expected;
            }
            caughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
            return null;
        }
    }

    private Throwable matchExpected(Throwable e) {
        if (expectedExceptionType.isInstance(e)) {
            return e;
        }
        Throwable cause = e.getCause();
        if (cause != null && expectedExceptionType.isInstance(cause)) {
            return cause;
        }
        return null;
    }

    public String getClassName(Class classType) {
        return remotePackers.getClassName(classType);
    }

    public boolean isSupportAndValidationType(Type classType) {
        return remotePackers.isSupportAndValidationType(classType);
    }
}
