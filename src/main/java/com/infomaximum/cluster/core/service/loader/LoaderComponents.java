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
	private final Map<Class<? extends Component>, List<Component>> roles;

	public LoaderComponents(TransportManager transportManager) throws Exception {
		this.transportManager=transportManager;
		this.roles = new ConcurrentHashMap<Class<? extends Component>, List<Component>>();

		init();
	}

	private void init() throws Exception {
		//TODO необходима правильная инициализация менеджера, в настоящий момент считаем, что приложение у нас одно поэтому инициализируем его прямо тут
		ManagerComponent managerRole = new ManagerComponent(transportManager, null);
		addRole(managerRole);
		managerRole.init();
	}

	//TODO необходима проверка на уникальность
	public Component initRole(Class<? extends Component> classRole, ComponentConfig config) throws Exception {
		Constructor constructor = classRole.getConstructor(TransportManager.class, ComponentConfig.class);
		if (constructor==null) throw new RuntimeException("Not found constructor in class subsystem: " + classRole);

		Component role = null;
		try {
			role = (Component)constructor.newInstance(transportManager, config);
			addRole(role);
			role.init();
			return role;
		} catch (Exception e) {
			if (role!=null) role.destroy();
			throw e;
		}
	}

	//TODO необходимо чтото придумать, нет отписку в случае кдиничной остановки подсистемы
	private synchronized void addRole(Component role) {
		List<Component> uuidRoles = roles.get(role.getClass());
		if (uuidRoles==null) {
			uuidRoles = new CopyOnWriteArrayList<Component>();
			roles.put(role.getClass(), uuidRoles);
		}
		uuidRoles.add(role);
	}

	//TODO необходимо чтото придумать, нет отписку в случае кдиничной остановки подсистемы
	private synchronized void removeRole(Component role) {
		List<Component> uuidRoles = roles.get(role.getClass());
		if (uuidRoles==null) return;
		uuidRoles.remove(role);
		if (uuidRoles.isEmpty()) roles.remove(role.getClass());
	}

	//TODO необходимо чтото придумать, нет отписку в случае кдиничной остановки подсистемы
	public <T extends Component> T getAnyRole(Class<? extends Component> classRole){
		List<Component> uuidRoles = roles.get(classRole);
		if (uuidRoles==null) return null;
		return (T) uuidRoles.get(RandomUtil.random.nextInt(uuidRoles.size()));
	}

	public List<Component> getRoles() {
		List<Component> list = new ArrayList<Component>();
		for (Collection<Component> items: roles.values()) {
			list.addAll(items);
		}
		return list;
	}

	public void destroy() {
		//Тут есть одна особенность, менеджер подсистем должен дестроится последним
		for (Map.Entry<Class<? extends Component>, List<Component>> entry: roles.entrySet()) {
			Class<? extends Component> classRole = entry.getKey();
			if (classRole == ManagerComponent.class) continue;

			List<Component> uuidRoles = entry.getValue();
			while (uuidRoles.size()>0) destroyRole(uuidRoles.get(0));
		}
		for (Map.Entry<Class<? extends Component>, List<Component>> entry: roles.entrySet()) {
			List<Component> uuidRoles = entry.getValue();
			while (uuidRoles.size()>0) destroyRole(uuidRoles.get(0));
		}
	}

	private void destroyRole(Component role) {
		role.destroy();
		transportManager.destroyTransport(role.getTransport());
		removeRole(role);
	}

	public static void validationRoleKey(String roleKey){
		if (roleKey.indexOf(':')==-1) throw new RuntimeException("Not valide roleKey: " + roleKey);
	}
}
