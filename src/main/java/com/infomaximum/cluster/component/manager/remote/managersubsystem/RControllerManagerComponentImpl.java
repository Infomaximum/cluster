package com.infomaximum.cluster.component.manager.remote.managersubsystem;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.manager.core.ManagerRegisterComponents;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.struct.RegistrationState;

/**
 * Created by kris on 02.11.16.
 */
public class RControllerManagerComponentImpl extends AbstractRController<ManagerComponent> implements RControllerManagerComponent {

	public RControllerManagerComponentImpl(ManagerComponent component) {
		super(component);
	}

	@Override
    public RegistrationState register(RuntimeComponentInfo componentInfo) {
        ManagerRegisterComponents registerComponent = component.getRegisterComponent();
        return registerComponent.registerActiveRole(componentInfo);
	}

	@Override
    public void unregister(int uniqueId) {
        ManagerRegisterComponents registerComponent = component.getRegisterComponent();
        registerComponent.unRegisterActiveRole(uniqueId);
	}
}
