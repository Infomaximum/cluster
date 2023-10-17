package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.remote.utils.RemoteControllerAnalysis;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.MethodKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kris on 28.10.16.
 */
public abstract class AbstractRController<TComponent extends Component> implements RController {

    private final static Logger log = LoggerFactory.getLogger(AbstractRController.class);

    protected final TComponent component;

    private final Map<Integer, Method> cacheMethods;//Кеш методов

    public AbstractRController(TComponent component) {
        this.component = component;

        cacheMethods = new HashMap<>();
        for (Class interfaceClazz : this.getClass().getInterfaces()) {
            if (!RController.class.isAssignableFrom(interfaceClazz)) continue;

            RemoteControllerAnalysis remoteControllerAnalysis = new RemoteControllerAnalysis(component, interfaceClazz);
            for (Method method : remoteControllerAnalysis.getMethods()) {
                int methodKey = MethodKey.calcMethodKey(method);
                if (cacheMethods.containsKey(methodKey)) {
                    throw new RuntimeException("Collision method keys: " + method.getName() + " and " + cacheMethods.get(methodKey) + " in " + interfaceClazz.getName() + ", you need to change the signature to avoid conflicts");
                }
                cacheMethods.put(methodKey, method);
            }
        }
    }

    @Override
    public final UUID getNodeRuntimeId() {
        return component.getRemotes().cluster.node.getRuntimeId();
    }

    @Override
    public final String getComponentUuid() {
        return component.getInfo().getUuid();
    }

    public Method getRemoteMethod(int methodKey) {
        return cacheMethods.get(methodKey);
    }

    public Remotes getRemotes() {
        return component.getRemotes();
    }
}
