package com.infomaximum.cluster.component.custom.remote.doublebraceinitialization;

import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.core.remote.AbstractRController;

import java.util.HashMap;

public class RDoubleBraceInitializationImpl extends AbstractRController<CustomComponent> implements RDoubleBraceInitialization {

    public RDoubleBraceInitializationImpl(CustomComponent component) {
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
