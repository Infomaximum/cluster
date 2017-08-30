package com.infomaximum.cluster.core.component.remote.notification;

import com.infomaximum.cluster.core.component.RuntimeRoleInfo;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.struct.Component;

/**
 * Created by kris on 02.11.16.
 */
public class RControllerNotificationImpl extends AbstractRController<Component> implements RControllerNotification {

	public RControllerNotificationImpl(Component role) {
		super(role);
	}

	@Override
	public void notificationRegisterRole(RuntimeRoleInfo subSystemInfo) {
		role.getActiveRoles().registerActiveRole(subSystemInfo);
	}

	@Override
	public void notificationUnRegisterRole(RuntimeRoleInfo subSystemInfo) {
		role.getActiveRoles().unRegisterActiveRole(subSystemInfo.key);
	}
}


