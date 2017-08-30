package com.infomaximum.cluster.core.remote.struct;

import com.infomaximum.cluster.struct.Component;
import net.minidev.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by kris on 28.10.16.
 */
public interface RemoteObject {

	public JSONObject serialize();

	public static boolean instanceOf(Class classType) {
		return RemoteObject.class.isAssignableFrom(classType);
	}

	public static <T extends RemoteObject> T deserialize(Component role, Class<T> classType, JSONObject json) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method method=null;
		for (Method iMethod: classType.getMethods()) {
			if (!iMethod.getName().equals("deserialize")) continue;
			if (!Modifier.isStatic(iMethod.getModifiers())) throw new NoSuchMethodException("Method: deserialize is not static in class: " + classType);
			method = iMethod;
			break;
		}
		if (method==null) throw new NoSuchMethodException("Not found method: deserialize in class: " + classType);

		switch (method.getParameterCount()){
			case 1:
				return (T) method.invoke(null, json);
			case 2:
				return (T) method.invoke(null, classType, json);
			case 3:
				return (T) method.invoke(null, role, classType, json);
			default:
				throw new NoSuchMethodException("Not found method: deserialize in class: " + classType);
		}
	}
}
