package com.infomaximum.cluster.component.manager.remote.managersubsystem;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.manager.core.RegisterComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.struct.ComponentInfos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by kris on 02.11.16.
 */
public class RControllerManagerComponentImpl extends AbstractRController<ManagerComponent> implements RControllerManagerComponent {

	public RControllerManagerComponentImpl(ManagerComponent component) {
		super(component);
	}

	@Override
	public ComponentInfos register(RuntimeComponentInfo componentInfo) {
		RegisterComponent registerComponent = component.getRegisterComponent();
        ArrayList<RuntimeComponentInfo> subSystemInfoList = registerComponent.registerActiveRole(componentInfo);
        return new ComponentInfos(subSystemInfoList);
	}

	@Override
	public void unregister(String componentKey) {
		RegisterComponent registerComponent = component.getRegisterComponent();
		registerComponent.unRegisterActiveRole(componentKey);
	}
}
