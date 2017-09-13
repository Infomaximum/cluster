package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.CacheClassForName;
import com.infomaximum.cluster.utils.ExecutorUtil;
import net.minidev.json.JSONObject;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by kris on 29.12.16.
 */
public class RemoteControllerInvocationHandler implements InvocationHandler {

    private final Component component;

    private final String targetRoleKey;
    private final Class<? extends RController> rControllerClass;
//    private final String targetRemoteControllerName;

    public RemoteControllerInvocationHandler(Component component, String targetRoleKey, Class<? extends RController> rControllerClass) {
        this.component = component;

        this.targetRoleKey = targetRoleKey;
        this.rControllerClass=rControllerClass;
//        this.targetRemoteControllerName = targetRemoteControllerName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return component.getTransport().request(targetRoleKey, rControllerClass, method, args);
//        if (method.getReturnType() == Future.class) {
//            CompletableFuture<Object> responseFuture = new CompletableFuture<Object>();
//            ExecutorUtil.executors.execute(() -> {
//                try {
//                    JSONObject request = packRequest(component, targetRemoteControllerName, method, args);
//                    JSONObject response = component.getTransport().request(targetRoleKey, request);
//                    responseFuture.complete(unpackResponse(component, method, response));
//                } catch (Exception e) {
//                    responseFuture.completeExceptionally(e);
//                }
//            });
//            return responseFuture;
//        } else {
//            JSONObject request = packRequest(component, targetRemoteControllerName, method, args);
//            JSONObject response = component.getTransport().request(targetRoleKey, request);
//            return unpackResponse(component, method, response);
//        }
    }


}
