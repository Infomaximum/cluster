package com.infomaximum.cluster.core.service.transport.executor;

import com.infomaximum.cluster.core.component.remote.notification.RControllerNotification;
import com.infomaximum.cluster.core.component.remote.notification.RControllerNotificationImpl;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.remote.utils.PackRemoteArgUtils;
import com.infomaximum.cluster.core.service.transport.struct.packet.TPacketResponse;
import com.infomaximum.cluster.struct.Component;
import net.minidev.json.JSONObject;
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
public class ExecutorTransport {

	private final static Logger log = LoggerFactory.getLogger(ExecutorTransport.class);

	protected final Component subSystem;
	private final Map<String, RController> hashRemoteController;

	public ExecutorTransport(Component subSystem) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		this.subSystem=subSystem;

		Reflections reflections = new Reflections(subSystem.getInfo().getUuid());

		this.hashRemoteController = new HashMap<String, RController>();

		//Добавляем обработчик нотификаций
		RControllerNotificationImpl remoteControllerNotification = new RControllerNotificationImpl(subSystem);
		hashRemoteController.put(RControllerNotification.class.getName(), remoteControllerNotification);

		for (Class<? extends AbstractRController> classRemoteController: reflections.getSubTypesOf(AbstractRController.class)){
			if (classRemoteController.isInterface()) continue;
			Constructor constructor;
			try {
				constructor = classRemoteController.getConstructor(subSystem.getClass());
			} catch (NoSuchMethodException e) {
				log.error("Not found constructor from: {}", classRemoteController, e);
				throw e;
			}
			if (constructor==null) throw new RuntimeException("Not found constructor in class controller: " + classRemoteController);
			AbstractRController rController = (AbstractRController)constructor.newInstance(subSystem);

			for (Class<? extends RController> classRController: getRControllerClasses(rController)) {
				hashRemoteController.put(classRController.getName(), rController);
			}
		}
	}

	public void registerRController(RController rController) {
		for (Class<? extends RController> classRController: getRControllerClasses(rController)) {
			hashRemoteController.put(classRController.getName(), rController);
		}
	}

	public Collection<Class<? extends RController>> getClassRControllers(){
		Collection<Class<? extends RController>> rControllers = new HashSet<Class<? extends RController>>();
		for (RController rController: hashRemoteController.values()) {
			Class clazz = rController.getClass();
			for (Class interfaceClass: clazz.getInterfaces()) {
				if (!RController.class.isAssignableFrom(interfaceClass)) continue;
				rControllers.add(interfaceClass);
			}
		}
		return rControllers;
	}

	public TPacketResponse execute(JSONObject request) {
		try {
			String controllerName = request.getAsString("controller");
			String methodName = request.getAsString("method");
			JSONObject parseArgs = (JSONObject) request.get("args");

			RController remoteController = hashRemoteController.get(controllerName);
			if (remoteController==null) {
				throw new RuntimeException("Not found remote controller, subsystem: " + subSystem + ", controller: " + controllerName + ", method: " + methodName);
			}
			Method method = ((AbstractRController)remoteController).getRemoteMethod(remoteController.getClass().getInterfaces()[0], methodName);
			if (method==null) {
				throw new RuntimeException("Not found remote method, subsystem: " + subSystem + ", controller: " + controllerName + ", method: " + methodName);
			}

			Class[] methodParameterTypes = method.getParameterTypes();
			Object[] methodParameters = new Object[methodParameterTypes.length];
			for (int i=0; i<methodParameterTypes.length; i++) {
				JSONObject parseValue = (JSONObject) parseArgs.get(String.valueOf(i));
				if (parseValue==null) {
					methodParameters[i] = null;
				} else {
					String classType = parseValue.getAsString("class");
					Object value = parseValue.get("value");
					methodParameters[i] = PackRemoteArgUtils.deserialize(subSystem, Class.forName(classType), value);
				}
			}

			Object oResponse;
			try {
				oResponse = method.invoke(remoteController, methodParameters);
			} catch (IllegalArgumentException e) {
				return new TPacketResponse(e, null);
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				if (targetException instanceof Exception) {
					return new TPacketResponse((Exception)targetException, null);
				} else {
					return new TPacketResponse(new Exception("Not support target exception", e), null);
				}
			}

			JSONObject response = new JSONObject();
			if (oResponse!=null) {
				response.put("result", PackRemoteArgUtils.serialize(oResponse));
				response.put("result_class", PackRemoteArgUtils.getClassName(oResponse.getClass()));
			}

			return new TPacketResponse(response);
		} catch (Exception e) {
			return new TPacketResponse(e, null);
		}
	}

	private static Set<Class<? extends RController>> getRControllerClasses(RController rController){
		Set<Class<? extends RController>> rControllerClasses = new HashSet<>();
		for (Class iClass: rController.getClass().getInterfaces()) {
			if (RController.class.isAssignableFrom(iClass)) rControllerClasses.add(iClass);
		}
		return rControllerClasses;
	}
}
