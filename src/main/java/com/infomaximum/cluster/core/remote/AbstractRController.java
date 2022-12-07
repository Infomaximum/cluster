package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.remote.utils.RemoteControllerAnalysis;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.EqualsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kris on 28.10.16.
 */
public abstract class AbstractRController<TComponent extends Component> implements RController {

    private final static Logger log = LoggerFactory.getLogger(AbstractRController.class);

    protected final TComponent component;

    private final Map<Class<? extends RController>, Map<String, List<Method>>> hashControllersRemoteMethods;//Хеш методов

    public AbstractRController(TComponent component) {
        this.component = component;

        hashControllersRemoteMethods = new HashMap<Class<? extends RController>, Map<String, List<Method>>>();
        for (Class interfaceClazz : this.getClass().getInterfaces()) {
            if (!RController.class.isAssignableFrom(interfaceClazz)) continue;

            RemoteControllerAnalysis remoteControllerAnalysis = new RemoteControllerAnalysis(component, interfaceClazz);
            hashControllersRemoteMethods.put(interfaceClazz, remoteControllerAnalysis.getMethods());
        }
    }

    @Override
    public final byte getNode() {
        return component.getRemotes().cluster.node;
    }

    @Override
    public final String getComponentUuid() {
        return component.getInfo().getUuid();
    }

    public Method getRemoteMethod(Class<? extends RController> remoteControllerClazz, String name, Class<?>[] parameterTypes) {
        Map<String, List<Method>> hashControllerRemoteMethods = hashControllersRemoteMethods.get(remoteControllerClazz);
        if (hashControllerRemoteMethods == null) return null;

        Method method = null;
        for (Method iMethod : hashControllerRemoteMethods.get(name)) {
            if (iMethod.getParameterCount() != parameterTypes.length) continue;

            boolean equals = true;
            for (int iArg = 0; iArg < parameterTypes.length; iArg++) {
                Class<?> iMethodArg = iMethod.getParameterTypes()[iArg];
                Class<?> methodArg = parameterTypes[iArg];

                //Если null, значит нет возможности сопоставить типы - идем дальше
                if (methodArg == null) continue;

                if (!(EqualsUtils.equalsType(iMethodArg, methodArg) || iMethodArg.isAssignableFrom(methodArg))) {
                    equals = false;
                    break;
                }
            }

            if (equals) {
                method = iMethod;
                break;
            }
        }
        return method;
    }

    public Remotes getRemotes() {
        return component.getRemotes();
    }

}
