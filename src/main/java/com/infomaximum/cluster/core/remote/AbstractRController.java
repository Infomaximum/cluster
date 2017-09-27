package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.anotation.DisableValidationRemoteMethod;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.EqualsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    protected AbstractRController(TComponent component) {
        this.component = component;

        hashControllersRemoteMethods = new HashMap<Class<? extends RController>, Map<String, List<Method>>>();
        for (Class interfaceClazz: this.getClass().getInterfaces()){
            if (!RController.class.isAssignableFrom(interfaceClazz)) continue;
            Map<String, List<Method>> hashMethods = new HashMap<String, List<Method>>();
            for (Method method: interfaceClazz.getDeclaredMethods()) {

                //Проверяем, что результат и аргументы сериализуемы
                if (!method.isAnnotationPresent(DisableValidationRemoteMethod.class)) {
                    validationRemoteMethod(interfaceClazz, method);
                }

                //Игнорируем права доступа
                method.setAccessible(true);

                List<Method> methods = hashMethods.get(method.getName());
                if (methods == null) {
                    methods = new ArrayList<Method>();
                    hashMethods.put(method.getName(), methods);
                }
                methods.add(method);
            }
            hashControllersRemoteMethods.put(interfaceClazz, hashMethods);
        }
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


    private void validationRemoteMethod(Class remoteControllerClazz, Method method) {
        //Валидируем return type
        if (!validationType(method.getGenericReturnType())) {
            throw new RuntimeException("Not valid return type: " + method.getGenericReturnType() + " in remote method: " + method.getName() + ", in controller: " + remoteControllerClazz);
        }

        //Валидируем аргументы
        for (Type genericType : method.getGenericParameterTypes()) {
            if (!validationType(genericType)) {
                throw new RuntimeException("Not valid argument: " + genericType + " remote method: " + method.getName() + ", in controller: " + remoteControllerClazz);
            }
        }
    }

    private boolean validationType(Type type) {
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
                boolean iValidation = validationType(iType);
                if (!iValidation) return false;
            }
        }

        return true;
    }
}
