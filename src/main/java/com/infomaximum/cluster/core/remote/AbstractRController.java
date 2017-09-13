package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.anotation.DisableValidationRemoteMethod;
import com.infomaximum.cluster.core.remote.struct.RController;
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

	private final Map<Class<? extends RController>, Map<String, Method>> hashControllersRemoteMethods;//Хеш методов

	protected AbstractRController(TComponent component) {
		this.component = component;

		hashControllersRemoteMethods = new HashMap<Class<? extends RController>, Map<String, Method>>();
		for (Class interfaceClazz: this.getClass().getInterfaces()){
			if (!RController.class.isAssignableFrom(interfaceClazz)) continue;
			Map<String, Method> hashMethods = new HashMap<String, Method>();
			for (Method method: interfaceClazz.getDeclaredMethods()) {

				//Проверяем, что результат и аргументы сериализуемы
				if (!method.isAnnotationPresent(DisableValidationRemoteMethod.class)) {
					RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();
					if (method.getReturnType()!=void.class && !remotePackerObjects.isSupportType(method.getReturnType())) {
						throw new RuntimeException("Not valid remote method: " + method.getName() + ", in controller: " + interfaceClazz);
					}
					for (Class arg: method.getParameterTypes()) {
						if (!remotePackerObjects.isSupportType(arg)) {
							throw new RuntimeException("Not valid remote method: " + method.getName() + ", in controller: " + interfaceClazz);
						}
					}
				}

				hashMethods.put(method.getName(), method);
			}
			hashControllersRemoteMethods.put(interfaceClazz, hashMethods);
		}
	}

	public Method getRemoteMethod(Class<? extends RController> remoteControllerClazz, String name) {
		Map<String, Method> hashControllerRemoteMethods = hashControllersRemoteMethods.get(remoteControllerClazz);
		if (hashControllerRemoteMethods==null) return null;
		return hashControllerRemoteMethods.get(name);
	}

	public Remotes getRemotes() {
		return component.getRemotes();
	}

}
