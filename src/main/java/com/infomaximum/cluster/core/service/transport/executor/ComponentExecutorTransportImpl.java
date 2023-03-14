package com.infomaximum.cluster.core.service.transport.executor;

import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.core.remote.ComponentRemotePacker;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.GlobalUniqueIdUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by kris on 14.09.16.
 */
public class ComponentExecutorTransportImpl implements ComponentExecutorTransport {

    private final static Logger log = LoggerFactory.getLogger(ComponentExecutorTransportImpl.class);

    protected final Component component;
    private final ComponentRemotePacker componentRemotePacker;

    private final Map<String, RController> hashRemoteController;

    private ComponentExecutorTransportImpl(Component component, Map<String, RController> hashRemoteController) {
        this.component = component;
        this.hashRemoteController = hashRemoteController;
        this.componentRemotePacker = component.getRemotes().getRemotePackerObjects();
    }

    @Override
    public HashSet<Class<? extends RController>> getClassRControllers() {
        HashSet<Class<? extends RController>> rControllers = new HashSet<Class<? extends RController>>();
        for (RController rController : hashRemoteController.values()) {
            Class clazz = rController.getClass();
            for (Class interfaceClass : clazz.getInterfaces()) {
                if (!RController.class.isAssignableFrom(interfaceClass)) continue;
                rControllers.add(interfaceClass);
            }
        }
        return rControllers;
    }

    @Override
    public Object execute(String rControllerClassName, String methodName, Object[] args) throws Exception {
        RController remoteController = getRemoteController(rControllerClassName);
        Method method = getMethod(remoteController, methodName, (args != null) ? args.length : 0);
        return execute(remoteController, method, args);
    }

    @Override
    public Result execute(String rControllerClassName, String methodName, byte[][] byteArgs) throws Exception {
        RController remoteController = getRemoteController(rControllerClassName);
        Method method = getMethod(remoteController, methodName, byteArgs.length);

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[byteArgs.length];
        for (int i = 0; i < byteArgs.length; i++) {
            Object arg = componentRemotePacker.deserialize(parameterTypes[i], byteArgs[i]);
            args[i] = arg;
        }

        try {
            Object result = execute(remoteController, method, args);

            return new Result(componentRemotePacker.serialize(result), null);
        } catch (Exception e) {
            return new Result(null, componentRemotePacker.serialize(e));
        }
    }

    private RController getRemoteController(String rControllerClassName) throws Exception {
        RController remoteController = hashRemoteController.get(rControllerClassName);
        if (remoteController == null) {
            throw component.getRemotes().cluster.getExceptionBuilder().buildMismatchRemoteApiNotFoundControllerException(
                    GlobalUniqueIdUtils.getNode(component.getUniqueId()), component.getUniqueId(),
                    rControllerClassName
            );
        } else {
            return remoteController;
        }
    }

    private Method getMethod(RController remoteController, String methodName, int methodParameterCount) throws Exception {
        Method method = ((AbstractRController) remoteController).getRemoteMethod(remoteController.getClass().getInterfaces()[0], methodName, methodParameterCount);
        if (method == null) {
            throw component.getRemotes().cluster.getExceptionBuilder().buildMismatchRemoteApiNotFoundMethodException(
                    GlobalUniqueIdUtils.getNode(component.getUniqueId()), component.getUniqueId(),
                    remoteController.getClass().getName(), methodName
            );
        }
        return method;
    }

    private Object execute(RController remoteController, Method method, Object[] args) throws Exception {
        Object result;
        try {
            result = method.invoke(remoteController, (Object[]) args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof Exception) {
                throw (Exception) targetException;
            } else {
                throw new RuntimeException("Not support target exception", targetException);
            }
        }

        //Валидируем ответ - он не должен быть анонимным
        if (result != null && result.getClass().isAnonymousClass()) {
            throw new ClusterException("RemoteController: " + remoteController.getClass() + " return is anonymous class, method: " + method.getName());
        }

        return result;
    }

    public static class Builder {

        private final Component component;
        private final Map<String, RController> hashRemoteController;

        public Builder(Component component) {
            this.component = component;
            this.hashRemoteController = new HashMap<>();
        }

        public Builder withRemoteController(AbstractRController rController) {
            for (Class<? extends RController> classRController : getRControllerClasses(rController)) {
                hashRemoteController.put(classRController.getName(), rController);
            }
            return this;
        }

        public ComponentExecutorTransportImpl build() {
            Reflections reflections;
            synchronized (Reflections.class) {
                reflections = new Reflections(component.getInfo().getUuid(), new Scanner[0]);
            }
            for (Class<? extends AbstractRController> classRemoteController : reflections.getSubTypesOf(AbstractRController.class)) {
                AbstractRController rController;
                try {
                    Constructor constructor = classRemoteController.getDeclaredConstructor(component.getClass());
                    constructor.setAccessible(true);
                    rController = (AbstractRController) constructor.newInstance(component);
                } catch (ReflectiveOperationException e) {
                    throw new ClusterException("Exception init remote controller: " + classRemoteController, e);
                }

                for (Class<? extends RController> classRController : getRControllerClasses(rController)) {
                    hashRemoteController.put(classRController.getName(), rController);
                }
            }

            return new ComponentExecutorTransportImpl(component, hashRemoteController);
        }


        private static Set<Class<? extends RController>> getRControllerClasses(RController rController) {
            Set<Class<? extends RController>> rControllerClasses = new HashSet<>();
            for (Class iClass : rController.getClass().getInterfaces()) {
                if (RController.class.isAssignableFrom(iClass)) rControllerClasses.add(iClass);
            }
            return rControllerClasses;
        }
    }
}
