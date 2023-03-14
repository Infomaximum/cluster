package com.infomaximum.cluster.component.custom1.remote.disablevalidation;

import com.infomaximum.cluster.anotation.DisableValidationRemoteMethod;
import com.infomaximum.cluster.core.remote.struct.RController;

/**
 * Created by kris on 13.09.17.
 */
public interface RControllerDisableValidation extends RController {

    @DisableValidationRemoteMethod
    public Object get(String value);

}
