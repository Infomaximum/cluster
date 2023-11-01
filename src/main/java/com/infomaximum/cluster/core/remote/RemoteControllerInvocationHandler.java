package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteControllerInvocationHandler implements InvocationHandler {

    private final static String METHOD_GET_NODE_RUNTIME_ID = "getNodeRuntimeId";
    private final static String METHOD_GET_COMPONENT_UUID = "getComponentUuid";

    private final static String METHOD_TO_STRING = "toString";

    private final Component sourceComponent;

    private final RemoteTarget target;
    private final Class<? extends RController> rControllerClass;

    public RemoteControllerInvocationHandler(Component sourceComponent, RemoteTarget target, Class<? extends RController> rControllerClass) {
        this.sourceComponent = sourceComponent;

        this.target = target;
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
            return target.nodeRuntimeId();
        } else if (METHOD_GET_COMPONENT_UUID.equals(method.getName()) && method.getParameters().length == 0) {
            return target.componentUuid();
        } else if (METHOD_TO_STRING.equals(method.getName()) && method.getParameters().length == 0) {
            return method.toString();
        } else {
            return sourceComponent.getTransport().request(target, rControllerClass, method, args);
        }
    }
}
