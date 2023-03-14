package com.infomaximum.cluster.component.custom1.remote.exception;

import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.core.remote.AbstractRController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.NotLinkException;

/**
 * Created by kris on 28.10.16.
 */
public class RControllerExceptionImpl extends AbstractRController<Custom1Component> implements RControllerException {

    private final static Logger log = LoggerFactory.getLogger(RControllerExceptionImpl.class);

    public RControllerExceptionImpl(Custom1Component component) {
        super(component);
    }


    @Override
    public String getException(String value) throws NotLinkException {
        throw new NotLinkException("fake");
    }
}
