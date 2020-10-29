package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by kris on 29.12.16.
 */
public class RemoteControllerInvocationHandler implements InvocationHandler {

    private final Component component;

    private final int targetComponentUniqueId;
    private final Class<? extends RController> rControllerClass;

    public RemoteControllerInvocationHandler(Component component, int targetComponentUniqueId, Class<? extends RController> rControllerClass) {
        this.component = component;

        this.targetComponentUniqueId = targetComponentUniqueId;
        this.rControllerClass = rControllerClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return component.getTransport().request(targetComponentUniqueId, rControllerClass, method, args);
    }

}
