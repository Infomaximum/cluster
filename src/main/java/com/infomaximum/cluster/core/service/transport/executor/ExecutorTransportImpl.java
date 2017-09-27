package com.infomaximum.cluster.core.service.transport.executor;

import com.infomaximum.cluster.core.component.remote.notification.RControllerNotification;
import com.infomaximum.cluster.core.component.remote.notification.RControllerNotificationImpl;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.core.remote.struct.RController;
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

    public ExecutorTransportImpl(Component component) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        this.component = component;

        Reflections reflections = new Reflections(component.getInfo().getUuid());

        this.hashRemoteController = new HashMap<String, RController>();

        //Добавляем обработчик нотификаций
        RControllerNotificationImpl remoteControllerNotification = new RControllerNotificationImpl(component);
        hashRemoteController.put(RControllerNotification.class.getName(), remoteControllerNotification);

        for (Class<? extends AbstractRController> classRemoteController : reflections.getSubTypesOf(AbstractRController.class)) {
            if (classRemoteController.isInterface()) continue;
            Constructor constructor;
            try {
                constructor = classRemoteController.getConstructor(component.getClass());
            } catch (NoSuchMethodException e) {
                log.error("Not found constructor from: {}", classRemoteController, e);
                throw e;
            }
            if (constructor == null)
                throw new RuntimeException("Not found constructor in class controller: " + classRemoteController);
            AbstractRController rController = (AbstractRController) constructor.newInstance(component);

            for (Class<? extends RController> classRController : getRControllerClasses(rController)) {
                hashRemoteController.put(classRController.getName(), rController);
            }
        }
    }

    public void registerRController(RController rController) {
        for (Class<? extends RController> classRController : getRControllerClasses(rController)) {
            hashRemoteController.put(classRController.getName(), rController);
        }
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

//	public TPacketResponse execute(JSONObject request) {
//		try {
//			String controllerName = request.getAsString("controller");
//			String methodName = request.getAsString("method");
//			JSONObject parseArgs = (JSONObject) request.get("args");
//
//			RController remoteController = hashRemoteController.get(controllerName);
//			if (remoteController==null) {
//				throw new RuntimeException("Not found remote controller, subsystem: " + component + ", controller: " + controllerName + ", method: " + methodName);
//			}
//			Method method = ((AbstractRController)remoteController).getRemoteMethod(remoteController.getClass().getInterfaces()[0], methodName);
//			if (method==null) {
//				throw new RuntimeException("Not found remote method, subsystem: " + component + ", controller: " + controllerName + ", method: " + methodName);
//			}
//
//			Class[] methodParameterTypes = method.getParameterTypes();
//			Object[] methodParameters = new Object[methodParameterTypes.length];
//			for (int i=0; i<methodParameterTypes.length; i++) {
//				JSONObject parseValue = (JSONObject) parseArgs.get(String.valueOf(i));
//				if (parseValue==null) {
//					methodParameters[i] = null;
//				} else {
//					String classType = parseValue.getAsString("class");
//					Object value = parseValue.get("value");
//					methodParameters[i] = component.getRemotes().getRemotePackerObjects().deserialize(Class.forName(classType), value);
//				}
//			}
//
//			Object oResponse;
//			try {
//				oResponse = method.invoke(remoteController, methodParameters);
//			} catch (IllegalArgumentException e) {
//				return new TPacketResponse(e, null);
//			} catch (InvocationTargetException e) {
//				Throwable targetException = e.getTargetException();
//				if (targetException instanceof Exception) {
//					return new TPacketResponse((Exception)targetException, null);
//				} else {
//					return new TPacketResponse(new Exception("Not support target exception", e), null);
//				}
//			}
//
//			JSONObject response = new JSONObject();
//			if (oResponse!=null) {
//				response.put("result", component.getRemotes().getRemotePackerObjects().serialize(oResponse));
//				response.put("result_class", component.getRemotes().getRemotePackerObjects().getClassName(oResponse.getClass()));
//			}
//
//			return new TPacketResponse(response);
//		} catch (Exception e) {
//			return new TPacketResponse(e, null);
//		}
//	}

    private static Set<Class<? extends RController>> getRControllerClasses(RController rController) {
        Set<Class<? extends RController>> rControllerClasses = new HashSet<>();
        for (Class iClass : rController.getClass().getInterfaces()) {
            if (RController.class.isAssignableFrom(iClass)) rControllerClasses.add(iClass);
        }
        return rControllerClasses;
    }
}
