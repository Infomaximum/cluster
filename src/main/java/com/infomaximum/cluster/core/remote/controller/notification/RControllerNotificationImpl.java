package com.infomaximum.cluster.core.remote.controller.notification;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.environment.EnvironmentComponents;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.struct.Component;

/**
 * Created by kris on 02.11.16.
 */
public class RControllerNotificationImpl extends AbstractRController<Component> implements RControllerNotification {

	public RControllerNotificationImpl(Component component) {
		super(component);
	}

	@Override
	public void notificationRegisterComponent(RuntimeComponentInfo componentInfo) {
        EnvironmentComponents environmentComponents = component.getEnvironmentComponents();
        if (environmentComponents instanceof ActiveComponents) {
            ActiveComponents activeComponents = (ActiveComponents) environmentComponents;
            activeComponents.registerActiveRole(componentInfo);
        }
	}

	@Override
    public void notificationUnRegisterComponent(int uniqueId) {
        EnvironmentComponents environmentComponents = component.getEnvironmentComponents();
        if (environmentComponents instanceof ActiveComponents) {
            ActiveComponents activeComponents = (ActiveComponents) environmentComponents;
            activeComponents.unRegisterActiveRole(uniqueId);
        }
	}
}


