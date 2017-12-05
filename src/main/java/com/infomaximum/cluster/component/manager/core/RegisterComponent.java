package com.infomaximum.cluster.component.manager.core;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
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
public class RegisterComponent implements ActiveComponents {

	private final static Logger log = LoggerFactory.getLogger(RegisterComponent.class);

	private final ManagerComponent managerComponent;

	private final Map<String, RuntimeComponentInfo> components;

	public RegisterComponent(ManagerComponent managerComponent) {
		this.managerComponent = managerComponent;
		this.components = new ConcurrentHashMap<>();

		//Регистрируем себя
		registerActiveRole(new RuntimeComponentInfo(
				managerComponent.getKey(),
				managerComponent.getInfo().getUuid(),
				managerComponent.getInfo().getVersion().toString(),
				managerComponent.isSingleton(),
				managerComponent.getTransport().getExecutor().getClassRControllers()
		));
	}

	@Override
	public Collection<RuntimeComponentInfo> registerActiveRole(RuntimeComponentInfo subSystemInfo) {
		String key = subSystemInfo.key;
		String uuid = subSystemInfo.uuid;
		boolean isSingleton = subSystemInfo.isSingleton;

		if (components.containsKey(key)) return components.values();

		Map<String, RuntimeComponentInfo> activeSubSystems;
		synchronized (components) {
			//Первым делом проверем на уникальность.
			if (isSingleton && getActiveSubSystemKeys().contains(uuid)) throw new RuntimeException("Subsystem: " + uuid + " does not support clusteringt");

			//Сохраняем список подсистем которые необходимо оповестить
			activeSubSystems = new HashMap<>(components);

			//Регистрируем
			components.put(key, subSystemInfo);
		}

		//Оповещаем все подсистемы о новом модуле
		for(String keySubSystem: activeSubSystems.keySet()) {
			managerComponent.getRemotes().getFromSSKey(keySubSystem, RControllerNotification.class).notificationRegisterComponent(subSystemInfo);
		}

		return components.values();
	}

	@Override
	public Collection<RuntimeComponentInfo> unRegisterActiveRole(String key) {
		if (!components.containsKey(key)) return components.values();

		RuntimeComponentInfo unRegisterSubSystemInfo;
		Map<String, RuntimeComponentInfo> activeSubSystems;
		synchronized (components) {
			unRegisterSubSystemInfo = components.remove(key);

			activeSubSystems = new HashMap<>(components);
		}

		//Oповещаем все подсистемы
		for(String keySubSystem: activeSubSystems.keySet()) {
			managerComponent.getRemotes().getFromSSKey(keySubSystem, RControllerNotification.class).notificationUnRegisterComponent(unRegisterSubSystemInfo);
		}

		return components.values();
	}

	@Override
	public Collection<RuntimeComponentInfo> getActiveSubSystems() {
		return components.values();
	}

	@Override
	public Collection<String> getActiveSubSystemKeys() {
		return new HashSet<String>(components.keySet());
	}

	@Override
	public Collection<String> getActiveSubSystemUuids() {
		HashSet<String> subSystemUuids = new HashSet<String>();
		for(RuntimeComponentInfo subSystemInfo: components.values()){
			subSystemUuids.add(subSystemInfo.uuid);
		}
		return subSystemUuids;
	}

	@Override
	public boolean isActiveSubSystem(String uuid) {
		return getActiveSubSystemUuids().contains(uuid);
	}
}
