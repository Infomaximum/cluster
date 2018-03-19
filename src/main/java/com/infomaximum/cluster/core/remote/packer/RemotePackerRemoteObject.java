package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.core.remote.RemotePackerObjects;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerRemoteObject implements RemotePacker<RemoteObject> {

    private final RemotePackerSerializable remotePackerSerializable;

    public RemotePackerRemoteObject() {
        this.remotePackerSerializable = new RemotePackerSerializable();
    }

    @Override
    public boolean isSupport(Class classType) {
        return RemoteObject.class.isAssignableFrom(classType);
    }

    @Override
    public void validation(Type classType) {
        RemoteObjectValidator.validation(classType);
    }

    @Override
    public byte[] serialize(Component component, RemoteObject value) {
        return remotePackerSerializable.serialize(component, value);
    }

    @Override
    public RemoteObject deserialize(Component component, Class classType, byte[] value) {
        return (RemoteObject) remotePackerSerializable.deserialize(component, classType, value);
    }

    public static class RemoteObjectValidator {

        private static final Set<Type> checkedClasses = ConcurrentHashMap.newKeySet();

        public static void validation(Type type) {
            if (checkedClasses.contains(type)) return;

            validationWorker(type, new ArrayList<>());

            //Валидация успешно прошла - запоминаем
            checkedClasses.add(type);
        }

        private static void validationWorker(Type type, List<String> trace) {

            //Сначала получаем изначальный raw class
            Class clazz = ReflectionUtils.getRawClass(type);

            //Валидируем raw class
            if (clazz.isPrimitive()) {
                return;
            } else if (RemoteObject.class.isAssignableFrom(clazz)) {

                //Проверяем все поля
                for (Field iField : clazz.getDeclaredFields()) {
                    if (Modifier.isTransient(iField.getModifiers())) {
                        continue;//Игнорируем поля помеченые как "не сериалезуемые"
                    }
                    if (iField.getType() == RemoteObject.class) {
                        continue;//Поле указывает на интерфейс RemoteObject - нечего проверять
                    }

                    Type iType = iField.getGenericType();

                    if (type == iType) continue;//Попытка уйти в рекурсию

                    validationWorker(iType, new ArrayList<String>(trace) {{
                        add(type.getTypeName());
                        add(iType.getTypeName());
                    }});

                    //Валидируем его дженерики
                    if (iType instanceof ParameterizedType) {
                        for (Type iiType : ((ParameterizedType) iType).getActualTypeArguments()) {
                            validationWorker(iiType, new ArrayList<String>(trace) {{
                                add(type.getTypeName());
                                add(iType.getTypeName());
                                add(iiType.getTypeName());
                            }});
                        }
                    }
                }

                //Проверяем родителя
                Class superClass = clazz.getSuperclass();
                if (superClass != null && superClass != Object.class) {
                    validationWorker(superClass, new ArrayList<String>(trace) {{
                        add(type.getTypeName());
                        add(superClass.getTypeName());
                    }});
                }

            } else if (Serializable.class.isAssignableFrom(clazz)) {
                return;
            } else {
                throw new RuntimeException("Not serializable class: " + type + ", trace: " + String.join(" => ", trace));
            }
        }

    }
}
