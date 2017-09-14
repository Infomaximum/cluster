package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.anotation.DisableValidationRemoteMethod;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
                    validationRemoteMethod(interfaceClazz, method);
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


    private void validationRemoteMethod(Class remoteControllerClazz, Method method) {
        //Валидируем return type
        if (!validationType1(method.getGenericReturnType())) {
            throw new RuntimeException("Not valid return type: " + method.getGenericReturnType() + " in remote method: " + method.getName() + ", in controller: " + remoteControllerClazz);
        }

        //Валидируем аргументы
        for (Type genericType : method.getGenericParameterTypes()) {
            if (!validationType1(genericType)) {
                throw new RuntimeException("Not valid argument: " + genericType + " remote method: " + method.getName() + ", in controller: " + remoteControllerClazz);
            }
        }
    }

//	private boolean validationType(Type type) {
//		RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();
////		if (type == void.class || type == Void.class) return true;
//
//		//Проверяем сам тип
////		if (!remotePackerObjects.isSupportType(type)) return false;
//
//		if (type!=null) validationType1(type);
//
//		return remotePackerObjects.isSupportType(type);
//	}

    private boolean validationType1(Type type) {
        if (type == void.class || type == Void.class) return true;

        //Сначала получаем изначальный raw class
        Class clazz;
        if (type instanceof ParameterizedType) {
            clazz = (Class) ((ParameterizedType) type).getRawType();
        } else {
            clazz = (Class) type;
        }

        //Валидируем raw class
        RemotePackerObjects remotePackerObjects = component.getRemotes().getRemotePackerObjects();
        boolean isValidation = remotePackerObjects.isSupportType(clazz);
        if (!isValidation) return false;

        //Валидируем если надо его дженерики
        if (type instanceof ParameterizedType) {
            for (Type iType : ((ParameterizedType) type).getActualTypeArguments()) {
                boolean iValidation = validationType1(iType);
                if (!iValidation) return false;
            }
        }

        return true;
    }
}
