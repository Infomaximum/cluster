package com.infomaximum.cluster.core.service.loader;

import com.infomaximum.cluster.component.manager.ManagerComponent;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.config.ComponentConfig;
import com.infomaximum.cluster.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by kris on 23.09.16.
 */
public class LoaderComponents {

	private final static Logger log = LoggerFactory.getLogger(LoaderComponents.class);

	private final TransportManager transportManager;

	//TODO необходимо чтото придумать, нет отписку в случае кдиничной остановки подсистемы
	private final Map<Class<? extends Component>, List<Component>> components;

	public LoaderComponents(TransportManager transportManager) throws Exception {
		this.transportManager=transportManager;
		this.components = new ConcurrentHashMap<Class<? extends Component>, List<Component>>();

		init();
	}

	private void init() throws Exception {
		//TODO необходима правильная инициализация менеджера, в настоящий момент считаем, что приложение у нас одно поэтому инициализируем его прямо тут
		ManagerComponent managerRole = new ManagerComponent(transportManager, null);
		addComponent(managerRole);
		managerRole.init();
	}

	//TODO необходима проверка на уникальность
	public Component initComponent(Class<? extends Component> classComponent, ComponentConfig config) throws Exception {
		Constructor constructor = classComponent.getConstructor(TransportManager.class, ComponentConfig.class);
		if (constructor==null) throw new RuntimeException("Not found constructor in class subsystem: " + classComponent);

		Component component = null;
		try {
			component = (Component)constructor.newInstance(transportManager, config);
			addComponent(component);
			component.init();
			return component;
		} catch (Exception e) {
			if (component!=null) component.destroy();
			throw e;
		}
	}

	//TODO необходимо чтото придумать, нет отписку в случае кдиничной остановки подсистемы
	private synchronized void addComponent(Component component) {
		List<Component> uuidRoles = components.get(component.getClass());
		if (uuidRoles==null) {
			uuidRoles = new CopyOnWriteArrayList<Component>();
			components.put(component.getClass(), uuidRoles);
		}
		uuidRoles.add(component);
	}

	//TODO необходимо чтото придумать, нет отписку в случае кдиничной остановки подсистемы
	private synchronized void removeComponent(Component component) {
		List<Component> components = this.components.get(component.getClass());
		if (components==null) return;
		components.remove(component);
		if (components.isEmpty()) this.components.remove(component.getClass());
	}

	//TODO необходимо чтото придумать, нет отписку в случае кдиничной остановки подсистемы
	public <T extends Component> T getAnyComponent(Class<? extends Component> classComponent){
		List<Component> components = this.components.get(classComponent);
		if (components==null) return null;
		return (T) components.get(RandomUtil.random.nextInt(components.size()));
	}

	public List<Component> getComponents() {
		List<Component> list = new ArrayList<Component>();
		for (Collection<Component> items: components.values()) {
			list.addAll(items);
		}
		return list;
	}

	public void destroy() {
		//Тут есть одна особенность, менеджер подсистем должен дестроится последним
		for (Map.Entry<Class<? extends Component>, List<Component>> entry: components.entrySet()) {
			Class<? extends Component> classRole = entry.getKey();
			if (classRole == ManagerComponent.class) continue;

			List<Component> uuidRoles = entry.getValue();
			while (uuidRoles.size()>0) destroyComponent(uuidRoles.get(0));
		}
		for (Map.Entry<Class<? extends Component>, List<Component>> entry: components.entrySet()) {
			List<Component> uuidRoles = entry.getValue();
			while (uuidRoles.size()>0) destroyComponent(uuidRoles.get(0));
		}
	}

	private void destroyComponent(Component component) {
		component.destroy();
		transportManager.destroyTransport(component.getTransport());
		removeComponent(component);
	}

	public static void validationRoleKey(String componentKey){
		if (componentKey.indexOf(':')==-1) throw new RuntimeException("Not valide componentKey: " + componentKey);
	}
}
