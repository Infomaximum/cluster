package com.infomaximum.cluster.component.custom.remote.complex;

import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.core.remote.AbstractRController;

public class RControllerComplexImpl extends AbstractRController<CustomComponent> implements RControllerComplex {

    protected RControllerComplexImpl(CustomComponent component) {
        super(component);
    }

    @Override
    public String get(String value, long time) {
        return value + time;
    }
}
