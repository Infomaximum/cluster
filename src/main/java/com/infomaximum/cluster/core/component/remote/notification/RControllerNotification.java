package com.infomaximum.cluster.core.component.remote.notification;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;

/**
 * Created by kris on 02.11.16.
 */
public interface RControllerNotification extends RController {

	public void notificationRegisterComponent(RuntimeComponentInfo componentInfo);

	public void notificationUnRegisterComponent(RuntimeComponentInfo componentInfo);
}


