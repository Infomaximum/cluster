package com.infomaximum.cluster.component.manager;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.manager.core.ManagerRegisterComponents;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.environment.EnvironmentComponents;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;

/**
 * Created by kris on 23.09.16.
 */
public class ManagerComponent extends Component {

	public static final Info INFO = new Info.Builder("com.infomaximum.cluster.component.manager")
			.withComponentClass(ManagerComponent.class)
			.build();

	private ManagerRegisterComponents registerComponent;

	public ManagerComponent(Cluster cluster) {
		super(cluster, COMPONENT_UNIQUE_ID_MANAGER);
	}

	@Override
	public void init(TransportManager transportManager) {
		super.init(transportManager);
		registerComponent = new ManagerRegisterComponents(this);

		//Регистрируем себя
		transportManager.registerTransport(getTransport());
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

	public ManagerRegisterComponents getRegisterComponent() {
		return registerComponent;
	}
}
