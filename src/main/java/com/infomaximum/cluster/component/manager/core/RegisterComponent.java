package com.infomaximum.cluster.component.manager.core;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.remote.controller.notification.RControllerNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
				managerComponent.getInfo(),
				managerComponent.isSingleton(),
				managerComponent.getTransport().getExecutor().getClassRControllers()
		));
	}

	@Override
    public ArrayList<RuntimeComponentInfo> registerActiveRole(RuntimeComponentInfo subSystemInfo) {
        String key = subSystemInfo.key;
		String uuid = subSystemInfo.info.getUuid();
		boolean isSingleton = subSystemInfo.isSingleton;

        if (components.containsKey(key)) return new ArrayList<>(components.values());

		Map<String, RuntimeComponentInfo> activeSubSystems;
		synchronized (components) {
			//Первым делом проверем на уникальность.
			if (isSingleton && getActiveComponentUUIDs().contains(uuid)) {
				throw new RuntimeException("Component: " + uuid + " does not support clustering");
			}

			//Сохраняем список подсистем которые необходимо оповестить
			activeSubSystems = new HashMap<>(components);

			//Регистрируем
			components.put(key, subSystemInfo);
		}

		//Оповещаем все подсистемы о новом модуле
		for(String keySubSystem: activeSubSystems.keySet()) {
			managerComponent.getRemotes().getFromCKey(keySubSystem, RControllerNotification.class).notificationRegisterComponent(subSystemInfo);
		}

        return new ArrayList<>(components.values());
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
			managerComponent.getRemotes().getFromCKey(keySubSystem, RControllerNotification.class).notificationUnRegisterComponent(unRegisterSubSystemInfo);
		}

		return components.values();
	}

	@Override
	public Collection<RuntimeComponentInfo> getActiveComponents() {
		return components.values();
	}

	@Override
	public Collection<String> getActiveComponentKeys() {
		return new HashSet<String>(components.keySet());
	}

	public Collection<String> getActiveComponentUUIDs() {
		return components.values().stream()
				.map(runtimeComponentInfo -> runtimeComponentInfo.info.getUuid())
				.collect(Collectors.toSet());
	}

	@Override
	public Collection<String> getActiveComponentUuids() {
		HashSet<String> subSystemUuids = new HashSet<String>();
		for(RuntimeComponentInfo subSystemInfo: components.values()){
			subSystemUuids.add(subSystemInfo.info.getUuid());
		}
		return subSystemUuids;
	}

	@Override
	public boolean isActiveComponent(String uuid) {
		return getActiveComponentUuids().contains(uuid);
	}
}
