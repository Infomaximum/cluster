package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.GlobalUniqueIdUtils;

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
        //Валидируем аргументы - они не должны быть анонимными
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg.getClass().isAnonymousClass()) {
                    throw new ClusterException("Is anonymous class: rController: " + rControllerClass.getName()
                            + ", method: " + method.getName() + ", arg(index): " + i);
                }
            }
        }

        if ("getNode".equals(method.getName()) && method.getParameters().length == 0) {
            return GlobalUniqueIdUtils.getNode(targetComponentUniqueId);
        } else if ("getComponentUuid".equals(method.getName()) && method.getParameters().length == 0) {
            ManagerComponent managerComponent = component.getRemotes().cluster.getAnyLocalComponent(ManagerComponent.class);
            return managerComponent.getRegisterComponent().get(targetComponentUniqueId).uuid;
        } else {
            return component.getTransport().request(targetComponentUniqueId, rControllerClass, method, args);
        }
    }

}
