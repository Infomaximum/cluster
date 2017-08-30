package com.infomaximum.cluster.component.manager.core;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeRoleInfo;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.remote.notification.RControllerNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kris on 23.09.16.
 */
public class RegisterRole implements ActiveComponents {

	private final static Logger log = LoggerFactory.getLogger(RegisterRole.class);

	private final ManagerComponent managerRole;

	private final Map<String, RuntimeRoleInfo> roles;

	public RegisterRole(ManagerComponent managerRole) {
		this.managerRole=managerRole;
		this.roles = new ConcurrentHashMap<String, RuntimeRoleInfo>();

		//Регистрируем себя
		registerActiveRole(new RuntimeRoleInfo(
				managerRole.getKey(),
				managerRole.getInfo().getUuid(),
				managerRole.isSingleton(),
				managerRole.getTransport().getExecutor().getClassRControllers()
		));
	}

	@Override
	public Collection<RuntimeRoleInfo> registerActiveRole(RuntimeRoleInfo subSystemInfo) {
		String key = subSystemInfo.key;
		String uuid = subSystemInfo.uuid;
		boolean isSingleton = subSystemInfo.isSingleton;

		if (roles.containsKey(key)) return roles.values();

		Map<String, RuntimeRoleInfo> activeSubSystems;
		synchronized (roles) {
			//Первым делом проверем на уникальность.
			if (isSingleton && getActiveSubSystemKeys().contains(uuid)) throw new RuntimeException("Subsystem: " + uuid + " does not support clusteringt");

			//Сохраняем список подсистем которые необходимо оповестить
			activeSubSystems = new HashMap<String, RuntimeRoleInfo>(roles);

			//Регистрируем
			roles.put(key, subSystemInfo);
		}

		//Оповещаем все подсистемы о новом модуле
		for(String keySubSystem: activeSubSystems.keySet()) {
			managerRole.getRemotes().getFromSSKey(keySubSystem, RControllerNotification.class).notificationRegisterRole(subSystemInfo);
		}

		return roles.values();
	}

	@Override
	public Collection<RuntimeRoleInfo> unRegisterActiveRole(String key) {
		if (!roles.containsKey(key)) return roles.values();

		RuntimeRoleInfo unRegisterSubSystemInfo;
		Map<String, RuntimeRoleInfo> activeSubSystems;
		synchronized (roles) {
			unRegisterSubSystemInfo = roles.remove(key);

			activeSubSystems = new HashMap<String, RuntimeRoleInfo>(roles);
		}

		//Oповещаем все подсистемы
		for(String keySubSystem: activeSubSystems.keySet()) {
			managerRole.getRemotes().getFromSSKey(keySubSystem, RControllerNotification.class).notificationUnRegisterRole(unRegisterSubSystemInfo);
		}

		return roles.values();
	}

	@Override
	public Collection<RuntimeRoleInfo> getActiveSubSystems() {
		return roles.values();
	}

	@Override
	public Collection<String> getActiveSubSystemKeys() {
		return new HashSet<String>(roles.keySet());
	}

	@Override
	public Collection<String> getActiveSubSystemUuids() {
		HashSet<String> subSystemUuids = new HashSet<String>();
		for(RuntimeRoleInfo subSystemInfo: roles.values()){
			subSystemUuids.add(subSystemInfo.uuid);
		}
		return subSystemUuids;
	}

	@Override
	public boolean isActiveSubSystem(String uuid) {
		return getActiveSubSystemUuids().contains(uuid);
	}
}
