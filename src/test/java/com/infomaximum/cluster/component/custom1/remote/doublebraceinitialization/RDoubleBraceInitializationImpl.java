package com.infomaximum.cluster.component.custom1.remote.doublebraceinitialization;

import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.core.remote.AbstractRController;

import java.util.HashMap;

public class RDoubleBraceInitializationImpl extends AbstractRController<Custom1Component> implements RDoubleBraceInitialization {

    public RDoubleBraceInitializationImpl(Custom1Component component) {
        super(component);
    }

    @Override
    public HashMap<String, String> failResultDoubleBraceInitialization() {
        return new HashMap<>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
    }

    @Override
    public void failArgDoubleBraceInitialization(HashMap<String, String> arg) {

    }
}
