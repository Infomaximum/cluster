package com.infomaximum.cluster.component.custom.remote.disablevalidation;

import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.component.custom.remote.exception.RControllerException;
import com.infomaximum.cluster.core.remote.AbstractRController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.NotLinkException;

/**
 * Created by kris on 28.10.16.
 */
public class RControllerDisableValidationImpl extends AbstractRController<CustomComponent> implements RControllerDisableValidation {

    private final static Logger log = LoggerFactory.getLogger(RControllerDisableValidationImpl.class);

	public RControllerDisableValidationImpl(CustomComponent component) {
		super(component);
	}

    @Override
    public Object get(String value) {
        return value;
    }
}
