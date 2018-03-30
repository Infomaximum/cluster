package com.infomaximum.cluster.core.remote.utils;

import com.infomaximum.cluster.anotation.DisableValidationRemoteMethod;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.Component;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class RemoteControllerAnalysis {

    private final Map<String, List<Method>> methods;

    public RemoteControllerAnalysis(Component component, Class<? extends RController> interfaceClazz) {
        if (!interfaceClazz.isInterface())
            throw new IllegalArgumentException("Class: " + interfaceClazz + " is not interface.");

        this.methods = new HashMap<>();
        for (Method method : interfaceClazz.getDeclaredMethods()) {

            //Проверяем, что результат и аргументы сериализуемы
            if (!method.isAnnotationPresent(DisableValidationRemoteMethod.class)) {
                RemoteControllerUtils.validationRemoteMethod(component, interfaceClazz, method);
            }

            //Игнорируем права доступа
            method.setAccessible(true);

            List<Method> itemMethods = methods.get(method.getName());
            if (itemMethods == null) {
                itemMethods = new ArrayList<>();
                methods.put(method.getName(), itemMethods);
            }
            itemMethods.add(method);
        }
    }

    public Map<String, List<Method>> getMethods() {
        return methods;
    }
}
