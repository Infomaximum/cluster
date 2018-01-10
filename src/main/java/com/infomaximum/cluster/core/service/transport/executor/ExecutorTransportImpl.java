package com.infomaximum.cluster.core.service.transport.executor;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.anotation.DisableAutoInit;
import com.infomaximum.cluster.core.remote.controller.notification.RControllerNotification;
import com.infomaximum.cluster.core.remote.controller.notification.RControllerNotificationImpl;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
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
public class ExecutorTransportImpl implements ExecutorTransport {

    private final static Logger log = LoggerFactory.getLogger(ExecutorTransportImpl.class);

    protected final Component component;
    private final Map<String, RController> hashRemoteController;

    private ExecutorTransportImpl(Component component, Map<String, RController> hashRemoteController) throws ClusterException {
        this.component = component;
        this.hashRemoteController = hashRemoteController;
    }

    @Override
    public Collection<Class<? extends RController>> getClassRControllers() {
        Collection<Class<? extends RController>> rControllers = new HashSet<Class<? extends RController>>();
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
        RController remoteController = hashRemoteController.get(rControllerClassName);
        if (remoteController == null)
            throw new RuntimeException("Not found remote controller, component: " + component + ", controller: " + rControllerClassName + ", method: " + methodName);

        Class<?>[] parameterTypes;
        if (args == null) {
            parameterTypes = new Class<?>[0];
        } else {
            parameterTypes = new Class<?>[args.length];
            for (int iArgs = 0; iArgs < args.length; iArgs++) {
                parameterTypes[iArgs] = (args[iArgs] != null) ? args[iArgs].getClass() : null;
            }
        }

        Method method = ((AbstractRController) remoteController).getRemoteMethod(remoteController.getClass().getInterfaces()[0], methodName, parameterTypes);
        if (method == null) {
            throw new RuntimeException("Not found remote method, subsystem: " + component + ", controller: " + rControllerClassName + ", method: " + methodName);
        }

        try {
            return method.invoke(remoteController, args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof Exception) {
                throw (Exception) targetException;
            } else {
                throw new RuntimeException("Not support target exception", targetException);
            }
        }
    }

    public static class Builder {

        private final Component component;
        private final Map<String, RController> hashRemoteController;

        public Builder(Component component) throws ClusterException {
            this.component = component;
            this.hashRemoteController = new HashMap<>();
        }

        public Builder withRemoteController(AbstractRController rController) {
            for (Class<? extends RController> classRController : getRControllerClasses(rController)) {
                hashRemoteController.put(classRController.getName(), rController);
            }
            return this;
        }

        public ExecutorTransportImpl build() throws ClusterException {

            //Добавляем обработчик нотификаций
            withRemoteController(new RControllerNotificationImpl(component));

            //Ищем автоматически
            Reflections reflections = new Reflections(component.getInfo().getUuid());
            for (Class<? extends AbstractRController> classRemoteController : reflections.getSubTypesOf(AbstractRController.class)) {
                if (classRemoteController.isInterface()) continue;
                if (classRemoteController.getAnnotation(DisableAutoInit.class) != null) continue;

                AbstractRController rController;
                try {
                    Constructor constructor = classRemoteController.getConstructor(component.getClass());
                    constructor.setAccessible(true);
                    rController = (AbstractRController) constructor.newInstance(component);
                } catch (ReflectiveOperationException e) {
                    throw new ClusterException("Exception init remote controller: " + classRemoteController, e);
                }

                for (Class<? extends RController> classRController : getRControllerClasses(rController)) {
                    hashRemoteController.put(classRController.getName(), rController);
                }
            }

            return new ExecutorTransportImpl(component, hashRemoteController);
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
