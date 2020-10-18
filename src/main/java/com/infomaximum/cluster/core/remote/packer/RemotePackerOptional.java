package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Created by user on 06.09.2017.
 * TODO Ulitin V. Когда будем разъезжаться по серверам реализовать
 */
public class RemotePackerOptional implements RemotePacker<Optional> {

    @Override
    public boolean isSupport(Class classType) {
        return (classType == Optional.class);
    }

    @Override
    public String getClassName(Class classType) {
        return Optional.class.getName();
    }

    @Override
    public void validation(Type classType) {
        //TODO не реализовано, реализовать проверку
    }

    @Override
    public byte[] serialize(Component component, Optional value) {
        //TODO не реализовано, реализовать через
//        future.whenComplete((s, throwable) -> {
//            log.debug("futureError: thenAccept");
//        });

        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional deserialize(Component component, Class classType, byte[] value) {
        //TODO не реализовано, реализовать через
//        future.whenComplete((s, throwable) -> {
//            log.debug("futureError: thenAccept");
//        });

        throw new RuntimeException("Not implemented");
    }
}
