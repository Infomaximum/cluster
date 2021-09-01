package com.infomaximum.cluster.core.service.componentuuid;

import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ComponentUuidManager {

    private final static Logger log = LoggerFactory.getLogger(Info.class);

    private final Map<Class<? extends Component>, String> uuids;

    public ComponentUuidManager() {
        uuids = new HashMap<>();
    }

    public String getUuid(Class<? extends Component> classComponent) {
        return uuids.computeIfAbsent(classComponent, aClass -> {
            com.infomaximum.cluster.anotation.Info aComponent = aClass.getAnnotation(com.infomaximum.cluster.anotation.Info.class);
            if (aComponent == null) {
                //TODO Ulitin V. Как только все модули перейдут на анотацию - заменить предупреждение на exception
                log.warn("Annotation 'Component' not found in: " + aClass);
                return aClass.getPackage().getName();
            }
            return aComponent.uuid();
        });
    }
}
