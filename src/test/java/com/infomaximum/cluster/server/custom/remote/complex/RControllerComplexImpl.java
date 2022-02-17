package com.infomaximum.cluster.server.custom.remote.complex;

import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.server.custom.CustomComponent;

public class RControllerComplexImpl extends AbstractRController<CustomComponent> implements RControllerComplex {

    protected RControllerComplexImpl(CustomComponent component) {
        super(component);
    }

    @Override
    public String get(String value, long time) {
        return value + time;
    }
}
