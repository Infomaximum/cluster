package com.infomaximum.cluster.component.manager.remote.managersubsystem;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.component.manager.core.RegisterRole;
import com.infomaximum.cluster.core.component.RuntimeRoleInfo;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.struct.ComponentInfos;

import java.util.Collection;

/**
 * Created by kris on 02.11.16.
 */
public class RControllerManagerRoleImpl extends AbstractRController<ManagerComponent> implements RControllerManagerRole {

	public RControllerManagerRoleImpl(ManagerComponent role) {
		super(role);
	}

	@Override
	public ComponentInfos register(RuntimeRoleInfo roleInfo) {
		RegisterRole registerSubSystem = role.getRegisterSubSystem();
		Collection<RuntimeRoleInfo> subSystemInfoList = registerSubSystem.registerActiveRole(roleInfo);
		return new ComponentInfos(subSystemInfoList);
	}

	@Override
	public void unregister(String roleKey) {
		RegisterRole registerSubSystem = role.getRegisterSubSystem();
		registerSubSystem.unRegisterActiveRole(roleKey);
	}
}
