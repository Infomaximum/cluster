package com.infomaximum.cluster.component.manager;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.core.ManagerRegisterComponents;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.environment.EnvironmentComponents;
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

	public static final int MANAGER_UNIQUE_ID = 0;

	private ManagerRegisterComponents registerComponent;

	public ManagerComponent(Cluster cluster) {
		super(cluster);
	}

	@Override
	public void init(TransportManager transportManager) throws ClusterException {
		super.init(transportManager);
		registerComponent = new ManagerRegisterComponents(this);

		//Регистрируем себя
		transportManager.registerTransport(getTransport());
	}

	@Override
	public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
		return new ExecutorTransportImpl.Builder(this).build();
	}

	//Логика регистрации у менеджера подсистем не стандартная
	@Override
	protected ActiveComponents registerComponent() {
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
	public EnvironmentComponents getEnvironmentComponents() {
		return registerComponent;
	}

	@Override
	public void destroying() throws ClusterException {
		// do nothing
	}

	public ManagerRegisterComponents getRegisterComponent() {
		return registerComponent;
	}
}
