package com.infomaximum.cluster.component.custom1.remote.complex;

import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.core.remote.AbstractRController;

public class RControllerComplexImpl extends AbstractRController<Custom1Component> implements RControllerComplex {

    protected RControllerComplexImpl(Custom1Component component) {
        super(component);
    }

    @Override
    public String get(String value, long time) {
        return value + time;
    }
}
