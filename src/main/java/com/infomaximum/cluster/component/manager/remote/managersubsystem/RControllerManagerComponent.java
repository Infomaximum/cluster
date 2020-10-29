package com.infomaximum.cluster.component.manager.remote.managersubsystem;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.RegistrationState;

/**
 * Created by kris on 02.11.16.
 */
public interface RControllerManagerComponent extends RController {

    RegistrationState register(RuntimeComponentInfo componentInfo);

    void unregister(int uniqueId);
}


