package com.infomaximum.cluster.component.manager;

import com.infomaximum.cluster.Version;
import com.infomaximum.cluster.component.manager.core.RegisterComponent;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.active.ActiveComponentsImpl;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.utils.ManifestUtil;

/**
 * Created by kris on 23.09.16.
 */
public class ManagerComponent extends Component {

	public static final Info INFO = new Info.Builder(ManagerComponent.class)
			.withUuid(ManagerComponent.class.getPackage().getName())
			.withEnvironmentVersion(new Version(0, 0, 0))
			.withVersion(ManifestUtil.getVersion(ManagerComponent.class))
			.build();

	public static final String KEY = INFO.getUuid() + ":" + "00000000-0000-0000-0000-000000000000";

	private RegisterComponent registerComponent;

	@Override
	public void load() throws ClusterException {
		registerComponent = new RegisterComponent(this);
	}

	@Override
	protected String generateKey() {
		return KEY;
	}

	@Override
	public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
		return new ExecutorTransportImpl(this);
	}

	//Логика регистрации у менеджера подсистем не стандартная
	@Override
	protected ActiveComponentsImpl registerComponent() {
		return null;
	}

	//Логика Снятия регистрации у менеджера подсистем не стандартная
	@Override
	protected void unregisterComponent() {}

	@Override
	public Info getInfo() {
		return INFO;
	}

	@Override
	public ActiveComponents getActiveRoles() {
		return registerComponent;
	}

	@Override
	public void destroying() throws ClusterException {
		// do nothing
	}

	public RegisterComponent getRegisterComponent() {
		return registerComponent;
	}
}
