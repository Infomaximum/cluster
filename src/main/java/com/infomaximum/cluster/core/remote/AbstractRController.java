package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.remote.utils.RemoteControllerAnalysis;
import com.infomaximum.cluster.struct.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kris on 28.10.16.
 */
public abstract class AbstractRController<TComponent extends Component> implements RController {

    private final static Logger log = LoggerFactory.getLogger(AbstractRController.class);

    protected final TComponent component;

    private final Map<String, Method> cacheMethods;//Кеш методов

    public AbstractRController(TComponent component) {
        this.component = component;

        cacheMethods = new HashMap<>();
        for (Class interfaceClazz : this.getClass().getInterfaces()) {
            if (!RController.class.isAssignableFrom(interfaceClazz)) continue;

            RemoteControllerAnalysis remoteControllerAnalysis = new RemoteControllerAnalysis(component, interfaceClazz);
            for (Method method : remoteControllerAnalysis.getMethods()) {
                String methodKey = getMethodKey(interfaceClazz, method.getName(), method.getParameterCount());
                cacheMethods.put(methodKey, method);
            }
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

    public Method getRemoteMethod(Class<? extends RController> remoteControllerClazz, String methodName, int methodParameterCount) {
        String methodKey = getMethodKey(remoteControllerClazz, methodName, methodParameterCount);
        return cacheMethods.get(methodKey);
    }

    public Remotes getRemotes() {
        return component.getRemotes();
    }

    private static String getMethodKey(Class<? extends RController> remoteControllerClazz, String methodName, int methodParameterCount) {
        return new StringBuilder().append(remoteControllerClazz.getName()).append(methodName).append(methodParameterCount).toString();
    }
}
