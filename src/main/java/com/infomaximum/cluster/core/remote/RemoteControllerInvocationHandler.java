package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.utils.PackRemoteArgUtils;
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
    private final String targetRemoteControllerName;

    public RemoteControllerInvocationHandler(Component component, String targetRoleKey, String targetRemoteControllerName) {
        this.component = component;

        this.targetRoleKey = targetRoleKey;
        this.targetRemoteControllerName = targetRemoteControllerName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getReturnType() == Future.class) {
            CompletableFuture<Object> responseFuture = new CompletableFuture<Object>();
            ExecutorUtil.executors.execute(() -> {
                try {
                    JSONObject request = packRequest(targetRemoteControllerName, method, args);
                    JSONObject response = component.getTransport().request(targetRoleKey, request);
                    responseFuture.complete(unpackResponse(component, method, response));
                } catch (Exception e) {
                    responseFuture.completeExceptionally(e);
                }
            });
            return responseFuture;
        } else {
            JSONObject request = packRequest(targetRemoteControllerName, method, args);
            JSONObject response = component.getTransport().request(targetRoleKey, request);
            return unpackResponse(component, method, response);
        }
    }

    private static JSONObject packRequest(String remoteControllerName, Method method, Object[] args) throws IOException {
        JSONObject request = new JSONObject();

        request.put("controller", remoteControllerName);
        request.put("method", method.getName());

        if (args!=null) {
            JSONObject requestDataArgs = new JSONObject();
            request.put("args", requestDataArgs);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null) continue;

                JSONObject requestArg = new JSONObject();
                requestArg.put("class", arg.getClass().getName());
                requestArg.put("value", PackRemoteArgUtils.serialize(arg));
                requestDataArgs.put(String.valueOf(i), requestArg);
            }
        }

        return request;
    }

    private static Object unpackResponse(Component component, Method method, JSONObject response) throws ReflectiveOperationException, IOException, RocksDBException {
        Object result = response.get("result");
        if (result==null) {
            return null;
        } else {
            String resultClass = response.getAsString("result_class");

            Class classReturnType;
            if (resultClass!=null) {
                classReturnType = CacheClassForName.get(response.getAsString("result_class"));
            } else {
                classReturnType = method.getReturnType();
            }

            return PackRemoteArgUtils.deserialize(component, classReturnType, result);
        }
    }
}
