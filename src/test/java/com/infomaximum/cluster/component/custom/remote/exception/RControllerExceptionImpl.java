package com.infomaximum.cluster.component.custom.remote.exception;

import com.infomaximum.cluster.component.custom.CustomComponent;
import com.infomaximum.cluster.component.custom.remote.future.RControllerFuture;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.utils.ExecutorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.NotLinkException;
import java.util.concurrent.CompletableFuture;

/**
 * Created by kris on 28.10.16.
 */
public class RControllerExceptionImpl extends AbstractRController<CustomComponent> implements RControllerException {

    private final static Logger log = LoggerFactory.getLogger(RControllerExceptionImpl.class);

	public RControllerExceptionImpl(CustomComponent component) {
		super(component);
	}


    @Override
    public String getException(String value) throws NotLinkException {
        throw new NotLinkException("fake");
    }
}
