package com.infomaximum.cluster.core.remote.utils;

import com.infomaximum.cluster.core.remote.ComponentRemotePacker;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.*;

public class RemoteControllerUtils {

    public static void validationRemoteMethod(Component component, Class remoteControllerClazz, Method method) {
        //Валидируем return type
        if (!validationType(component, method.getGenericReturnType())) {
            throw new RuntimeException("Not valid return type: " + method.getGenericReturnType() + " in remote method: " + method.getName() + ", in controller: " + remoteControllerClazz);
        }

        //Валидируем аргументы
        for (Type genericType : method.getGenericParameterTypes()) {
            if (!validationType(component, genericType)) {
                throw new RuntimeException("Not valid argument: " + genericType + " remote method: " + method.getName() + ", in controller: " + remoteControllerClazz);
            }
        }
    }

    public static boolean validationType(Component component, Type type) {
        if (type == void.class || type == Void.class) return true;

        //Сначала получаем изначальный raw class
        Class clazz;
        if (type instanceof ParameterizedType) {
            clazz = (Class) ((ParameterizedType) type).getRawType();
        } else if (type instanceof TypeVariable) {
            for (Type iType : ((TypeVariable) type).getBounds()) {
                boolean iValidation = validationType(component, iType);
                if (!iValidation) return false;
            }
            return true;
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            for (Type iType : wildcardType.getLowerBounds()) {
                boolean iValidation = validationType(component, iType);
                if (!iValidation) return false;
            }
            for (Type iType : wildcardType.getUpperBounds()) {
                boolean iValidation = validationType(component, iType);
                if (!iValidation) return false;
            }
            return true;
        } else {
            clazz = (Class) type;
        }

        //Валидируем raw class
        ComponentRemotePacker componentRemotePacker = component.getRemotes().getRemotePackerObjects();
        boolean isValidation = componentRemotePacker.isSupportAndValidationType(type);
        if (!isValidation) return false;

        //Валидируем если надо его дженерики
        if (type instanceof ParameterizedType) {
            for (Type iType : ((ParameterizedType) type).getActualTypeArguments()) {
                boolean iValidation = validationType(component, iType);
                if (!iValidation) return false;
            }
        }

        return true;
    }

}
