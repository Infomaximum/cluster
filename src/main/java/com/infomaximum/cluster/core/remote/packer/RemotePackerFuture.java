package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerFuture implements RemotePacker<CompletableFuture> {

    @Override
    public boolean isSupport(Class classType) {
        return (classType == CompletableFuture.class);
    }

    @Override
    public String getClassName(Class classType) {
        return CompletableFuture.class.getName();
    }

    @Override
    public void validation(Type classType) {
        //TODO не реализовано, реализовать проверку
    }

    @Override
    public byte[] serialize(Component component, CompletableFuture value) {
        //TODO не реализовано, реализовать через
//        future.whenComplete((s, throwable) -> {
//            log.debug("futureError: thenAccept");
//        });

        throw new RuntimeException("Not implemented");
    }

    @Override
    public CompletableFuture deserialize(Component component, Class classType, byte[] value) {
        //TODO не реализовано, реализовать через
//        future.whenComplete((s, throwable) -> {
//            log.debug("futureError: thenAccept");
//        });

        throw new RuntimeException("Not implemented");
    }
}
