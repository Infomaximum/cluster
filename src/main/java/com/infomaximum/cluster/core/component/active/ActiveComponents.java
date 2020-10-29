package com.infomaximum.cluster.core.component.active;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.component.environment.EnvironmentComponents;
import com.infomaximum.cluster.struct.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kris on 04.10.16.
 */
public class ActiveComponents implements EnvironmentComponents {

	private final Component component;

	private final Map<Integer, RuntimeComponentInfo> componentInfos;

	public ActiveComponents(Component component, Collection<RuntimeComponentInfo> activeSubSystems) {
		this.component = component;

		this.componentInfos = new ConcurrentHashMap<>();
		for (RuntimeComponentInfo subSystemInfo : activeSubSystems) {
			this.componentInfos.put(subSystemInfo.uniqueId, subSystemInfo);
		}
	}

	public Collection<RuntimeComponentInfo> registerActiveRole(RuntimeComponentInfo subSystemInfo) {
		this.componentInfos.put(subSystemInfo.uniqueId, subSystemInfo);
		return componentInfos.values();
	}

	public Collection<RuntimeComponentInfo> unRegisterActiveRole(int uniqueId) {
		this.componentInfos.remove(uniqueId);
		return componentInfos.values();
	}

	@Override
	public Collection<RuntimeComponentInfo> getActiveComponents() {
		return componentInfos.values();
	}

	public Collection<String> getActiveComponentUuids() {
		HashSet<String> subSystemUuids = new HashSet<>();
		for (RuntimeComponentInfo subSystemInfo : componentInfos.values()) {
			subSystemUuids.add(subSystemInfo.info.getUuid());
		}
		return subSystemUuids;
	}
}
