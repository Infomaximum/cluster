package com.infomaximum.cluster.core.component.active;

import com.infomaximum.cluster.core.component.RuntimeRoleInfo;
import com.infomaximum.cluster.struct.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kris on 04.10.16.
 */
public class ActiveComponentsImpl implements ActiveComponents {

	private final Component role;

	private final Map<String, RuntimeRoleInfo> roleInfos;

	public ActiveComponentsImpl(Component role, Collection<RuntimeRoleInfo> activeSubSystems) {
		this.role = role;

		this.roleInfos = new ConcurrentHashMap<String, RuntimeRoleInfo>();
		for (RuntimeRoleInfo subSystemInfo: activeSubSystems){
			this.roleInfos.put(subSystemInfo.key, subSystemInfo);
		}
	}

	@Override
	public Collection<RuntimeRoleInfo> registerActiveRole(RuntimeRoleInfo subSystemInfo) {
		this.roleInfos.put(subSystemInfo.key, subSystemInfo);
		return roleInfos.values();
	}

	@Override
	public Collection<RuntimeRoleInfo> unRegisterActiveRole(String key) {
		this.roleInfos.remove(key);
		return roleInfos.values();
	}

	@Override
	public Collection<RuntimeRoleInfo> getActiveSubSystems() {
		return roleInfos.values();
	}

	@Override
	public Collection<String> getActiveSubSystemKeys(){
		return roleInfos.keySet();
	}

	@Override
	public Collection<String> getActiveSubSystemUuids() {
		HashSet<String> subSystemUuids = new HashSet<String>();
		for(RuntimeRoleInfo subSystemInfo: roleInfos.values()){
			subSystemUuids.add(subSystemInfo.uuid);
		}
		return subSystemUuids;
	}

	public boolean isActiveSubSystem(String uuid)  {
		return getActiveSubSystemUuids().contains(uuid);
	}
}
