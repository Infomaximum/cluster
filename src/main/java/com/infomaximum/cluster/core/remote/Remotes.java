package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by kris on 28.10.16.
 */
public class Remotes {

	private final static Logger log = LoggerFactory.getLogger(Remotes.class);

	public final Component component;

	private final RemotePackerObjects remotePackerObjects;

	public Remotes(Component component) {
		this.component = component;
		this.remotePackerObjects = new RemotePackerObjects(this);
	}

	public RemotePackerObjects getRemotePackerObjects() {
		return remotePackerObjects;
	}

	public <T extends RController> T getFromSSKey(String subSystemKey, Class<T> remoteControllerClazz) {
		//Валидируем ключ подсистемы
		if (subSystemKey.indexOf(':')==-1) throw new RuntimeException("Not valide componentKey: " + subSystemKey);

		//Кешировать proxy remoteController не получается т.к. Proxy.newProxyInstance может вернуться переиспользуемый объект в котором _properties уже есть значения и мы их затираем
		RController remoteController = (RController) Proxy.newProxyInstance(
				remoteControllerClazz.getClassLoader(), new Class[]{remoteControllerClazz},
				new RemoteControllerInvocationHandler(component, subSystemKey, remoteControllerClazz)
		);

		return (T)remoteController;
	}

	public <T extends RController> T getFromSSUuid(String uuid, Class<T> remoteControllerClazz){
		List<String> pretendents = new ArrayList<>();
		for (RuntimeComponentInfo componentInfo : component.getActiveComponents().getActiveSubSystems()) {
			String subSystemKey = componentInfo.key;
			String subSystemUuid = componentInfo.uuid;

			if (subSystemUuid.equals(uuid)) pretendents.add(subSystemKey);
		}
		if (pretendents.isEmpty()) throw new RuntimeException("Not found remote subsystem: " + uuid);
		return getFromSSKey(pretendents.get(RandomUtil.random.nextInt(pretendents.size())), remoteControllerClazz);
	}

	public <T extends RController> T get(Class<? extends Component> classRole, Class<T> remoteControllerClazz) throws NoSuchFieldException, IllegalAccessException {
		Info info = (Info) classRole.getField("INFO").get(null);
		return getFromSSUuid(info.getUuid(), remoteControllerClazz);
	}

	public <T extends RController> T get(Class<T> clazz){
		return null;
	}

	public <T extends RController> Collection<T> getControllers(Class<? extends Component> roleClass, Class<T> classController){
		throw new RuntimeException("Не реализованно");
	}

	public <T extends RController> Collection<T> getControllers(Class<T> remoteClassController){
		List<T> controllers = new ArrayList<>();
		for (RuntimeComponentInfo componentInfo : component.getActiveComponents().getActiveSubSystems()) {
			if (componentInfo.getClassNameRControllers().contains(remoteClassController.getName())) {
				//Нашли подсиситему в которой зарегистрирован этот контроллер
				controllers.add(getFromSSKey(componentInfo.key, remoteClassController));
			}
		}
		return controllers;
	}
}
