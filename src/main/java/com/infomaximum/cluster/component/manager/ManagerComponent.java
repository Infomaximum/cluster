package com.infomaximum.cluster.component.manager;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.core.RegisterComponent;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.active.ActiveComponentsImpl;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;

/**
 * Created by kris on 23.09.16.
 */
public class ManagerComponent extends Component {

	public static final Info INFO = new Info.Builder("com.infomaximum.cluster.component.manager")
			.withComponentClass(ManagerComponent.class)
			.build();

	public static final String KEY = INFO.getUuid() + ":" + "00000000-0000-0000-0000-000000000000";

	private RegisterComponent registerComponent;

	public ManagerComponent(Cluster cluster) {
		super(cluster);
	}

	@Override
	public void init(TransportManager transportManager) throws ClusterException {
		super.init(transportManager);
		registerComponent = new RegisterComponent(this);
	}

	@Override
	protected String generateKey() {
		return KEY;
	}

	@Override
	public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
		return new ExecutorTransportImpl.Builder(this).build();
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
	public ActiveComponents getActiveComponents() {
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
