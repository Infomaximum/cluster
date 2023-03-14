package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.exception.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.List;

public class RemotePackerObject {
    private final List<RemotePacker> remotePackers;

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public RemotePackerObject(List<RemotePacker> remotePackers, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.remotePackers = remotePackers;
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    public byte[] serialize(Component component, Object value) {
        for (RemotePacker remotePacker : remotePackers) {
            if (remotePacker.isSupport(value.getClass())) {
                byte[] result = remotePacker.serialize(component, value);
                assertResultSerialize(result, value);
                return result;
            }
        }
        throw new ClusterRemotePackerException();
    }

    public Object deserialize(Component component, Class classType, byte[] value) {
        for (RemotePacker remotePackerObject : remotePackers) {
            if (remotePackerObject.isSupport(classType))
                return remotePackerObject.deserialize(component, classType, value);
        }
        throw new ClusterRemotePackerException();
    }

    public String getClassName(Class classType) {
        for (RemotePacker remotePackerObject : remotePackers) {
            if (remotePackerObject.isSupport(classType)) return remotePackerObject.getClassName(classType);
        }
        throw new ClusterRemotePackerException();
    }

    public boolean isSupportAndValidationType(Type classType) {
        for (RemotePacker remotePackerObject : remotePackers) {
            if (remotePackerObject.isSupport(ReflectionUtils.getRawClass(classType))) {
                //Валидируем класс
                remotePackerObject.validation(classType);
                return true;
            }
        }
        return false;
    }

    private void assertResultSerialize(byte[] result, Object value) {
        if (result.length == 0) {
            uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), new IllegalArgumentException("Критическая ошибка! Value: " + value));
        }
    }
}
