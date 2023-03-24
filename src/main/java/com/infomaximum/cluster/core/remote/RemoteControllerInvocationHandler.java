package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.GlobalUniqueIdUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteControllerInvocationHandler implements InvocationHandler {

    private final static String METHOD_GET_NODE = "getNode";
    private final static String METHOD_GET_COMPONENT_UUID = "getComponentUuid";

    private final static String METHOD_TO_STRING = "toString";

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

        if (METHOD_GET_NODE.equals(method.getName()) && method.getParameters().length == 0) {
            return GlobalUniqueIdUtils.getNode(targetComponentUniqueId);
        } else if (METHOD_GET_COMPONENT_UUID.equals(method.getName()) && method.getParameters().length == 0) {
            ManagerComponent managerComponent = component.getRemotes().cluster.getAnyLocalComponent(ManagerComponent.class);
            RuntimeComponentInfo runtimeComponentInfo = managerComponent.getRegisterComponent().get(targetComponentUniqueId);
            if (runtimeComponentInfo == null) {
                throw component.getRemotes().cluster.getExceptionBuilder().buildRemoteComponentUnavailableException(GlobalUniqueIdUtils.getNode(targetComponentUniqueId), targetComponentUniqueId, null, 0, null);
            }
            return runtimeComponentInfo.uuid;
        } else if (METHOD_TO_STRING.equals(method.getName()) && method.getParameters().length == 0) {
            return method.toString();
        } else {
            return component.getTransport().request(targetComponentUniqueId, rControllerClass, method, args);
        }
    }
}
