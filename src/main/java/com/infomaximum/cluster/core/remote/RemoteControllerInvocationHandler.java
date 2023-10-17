package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.network.LocationRuntimeComponent;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class RemoteControllerInvocationHandler implements InvocationHandler {

    private final static String METHOD_GET_NODE_RUNTIME_ID = "getNodeRuntimeId";
    private final static String METHOD_GET_COMPONENT_UUID = "getComponentUuid";

    private final static String METHOD_TO_STRING = "toString";

    private final Component component;

    private final UUID targetNodeRuntimeId;
    private final int targetComponentId;
    private final Class<? extends RController> rControllerClass;

    public RemoteControllerInvocationHandler(Component component, UUID targetNodeRuntimeId, int targetComponentId, Class<? extends RController> rControllerClass) {
        this.component = component;

        this.targetNodeRuntimeId = targetNodeRuntimeId;
        this.targetComponentId = targetComponentId;
        this.rControllerClass = rControllerClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //Валидируем аргументы - они не должны быть анонимными
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg != null && arg.getClass().isAnonymousClass()) {
                    throw new ClusterException("Is anonymous class: rController: " + rControllerClass.getName()
                            + ", method: " + method.getName() + ", arg(index): " + i);
                }
            }
        }

        if (METHOD_GET_NODE_RUNTIME_ID.equals(method.getName()) && method.getParameters().length == 0) {
            return targetNodeRuntimeId;
        } else if (METHOD_GET_COMPONENT_UUID.equals(method.getName()) && method.getParameters().length == 0) {
            LocationRuntimeComponent runtimeComponentInfo = component.getTransport().getNetworkTransit().getManagerRuntimeComponent().get(targetNodeRuntimeId, targetComponentId);
            if (runtimeComponentInfo == null) {
                throw component.getRemotes().cluster.getExceptionBuilder().buildRemoteComponentUnavailableException(targetNodeRuntimeId, targetComponentId, null, 0, null);
            }
            return runtimeComponentInfo.component().uuid;
        } else if (METHOD_TO_STRING.equals(method.getName()) && method.getParameters().length == 0) {
            return method.toString();
        } else {
            return component.getTransport().request(targetNodeRuntimeId, targetComponentId, rControllerClass, method, args);
        }
    }
}
