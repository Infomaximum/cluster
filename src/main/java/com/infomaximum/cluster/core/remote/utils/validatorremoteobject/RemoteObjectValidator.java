package com.infomaximum.cluster.core.remote.utils.validatorremoteobject;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import com.infomaximum.cluster.utils.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteObjectValidator {

    private static final Set<Type> checkedClasses = ConcurrentHashMap.newKeySet();

    public static ResultValidator validation(Type type) {
        if (checkedClasses.contains(type)) return ResultValidator.SUCCESS;

        ResultValidator resultValidator = validationWorker(type, new ArrayList<>());

        if (resultValidator.isSuccess()) {
            //Валидация успешно прошла - запоминаем
            checkedClasses.add(type);
        }

        return resultValidator;
    }

    private static ResultValidator validationWorker(Type type, List<String> trace) {

        //Сначала получаем изначальный raw class
        Class clazz = ReflectionUtils.getRawClass(type);

        //Валидируем raw class
        if (clazz.isPrimitive()) {
            return ResultValidator.SUCCESS;
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

                if (type == iType) continue;//Рекурсия - не стоит проверять самого себя

                ResultValidator iResultValidator = validationWorker(iType, new ArrayList<String>(trace) {{
                    add(type.getTypeName());
                    add(iType.getTypeName());
                }});
                if (!iResultValidator.isSuccess()) return iResultValidator;

                //Валидируем его дженерики
                if (iType instanceof ParameterizedType) {
                    for (Type iiType : ((ParameterizedType) iType).getActualTypeArguments()) {

                        //Сравнение через type == iiType всегда возвращает false, т.к. ссылки разные
                        if (type.getTypeName().equals(iiType.getTypeName())) continue;

                        ResultValidator gResultValidator = validationWorker(iiType, new ArrayList<String>(trace) {{
                            add(type.getTypeName());
                            add(iType.getTypeName());
                            add(iiType.getTypeName());
                        }});
                        if (!gResultValidator.isSuccess()) return gResultValidator;
                    }
                }
            }

            //Проверяем родителя
            Class superClass = clazz.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                ResultValidator pResultValidator = validationWorker(superClass, new ArrayList<String>(trace) {{
                    add(type.getTypeName());
                    add(superClass.getTypeName());
                }});
                if (!pResultValidator.isSuccess()) return pResultValidator;
            }

            return ResultValidator.SUCCESS;
        } else if (Serializable.class.isAssignableFrom(clazz)) {
            return ResultValidator.SUCCESS;
        } else {
            return ResultValidator.buildFailResultValidator(type, trace);
        }
    }

}
