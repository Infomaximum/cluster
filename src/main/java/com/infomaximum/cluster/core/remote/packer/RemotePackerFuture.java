package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.struct.Component;

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
    public Object serialize(Component component, CompletableFuture value) {
        //TODO не реализовано, реализовать через
//        future.whenComplete((s, throwable) -> {
//            log.debug("futureError: thenAccept");
//        });

        throw new RuntimeException("Not implemented");
    }

    @Override
    public CompletableFuture deserialize(Component component, Class classType, Object value) {
        //TODO не реализовано, реализовать через
//        future.whenComplete((s, throwable) -> {
//            log.debug("futureError: thenAccept");
//        });

        throw new RuntimeException("Not implemented");
    }
}
