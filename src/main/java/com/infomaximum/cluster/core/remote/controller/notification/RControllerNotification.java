package com.infomaximum.cluster.core.remote.controller.notification;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;

/**
 * Created by kris on 02.11.16.
 */
public interface RControllerNotification extends RController {

	void notificationRegisterComponent(RuntimeComponentInfo componentInfo);

    void notificationUnRegisterComponent(int id);
}


