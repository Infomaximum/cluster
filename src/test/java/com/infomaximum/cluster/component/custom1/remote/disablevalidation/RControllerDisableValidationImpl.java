package com.infomaximum.cluster.component.custom1.remote.disablevalidation;

import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.core.remote.AbstractRController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 28.10.16.
 */
public class RControllerDisableValidationImpl extends AbstractRController<Custom1Component> implements RControllerDisableValidation {

    private final static Logger log = LoggerFactory.getLogger(RControllerDisableValidationImpl.class);

    public RControllerDisableValidationImpl(Custom1Component component) {
        super(component);
    }

    @Override
    public Object get(String value) {
        return value;
    }
}
