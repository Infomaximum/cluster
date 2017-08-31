package com.infomaximum.cluster.component.manager;

import com.infomaximum.cluster.component.manager.core.RegisterComponent;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.active.ActiveComponentsImpl;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.struct.config.ComponentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 23.09.16.
 */
public class ManagerComponent extends Component {

	private final static Logger log = LoggerFactory.getLogger(ManagerComponent.class);

	public static final Info INFO = new Info.Builder(ManagerComponent.class)
			.build();

	public static final String KEY = INFO.getUuid() + ":" + "00000000-0000-0000-0000-000000000000";


	private RegisterComponent registerComponent;

	public ManagerComponent(TransportManager transportManager, ComponentConfig config) throws Exception {
		super(transportManager, config);
	}

	@Override
	public void load() throws Exception {
		registerComponent = new RegisterComponent(this);
	}

	@Override
	protected String generateKey(ComponentConfig config) {
		return KEY;
	}

	@Override
	public ExecutorTransport initExecutorTransport() throws ReflectiveOperationException {
		return new ExecutorTransport(this);
	}

	//Логика регистрации у менеджера подсистем не стандартная
	@Override
	protected ActiveComponentsImpl registerComponent() throws Exception {
		return null;
	}

	//Логика Снятия регистрации у менеджера подсистем не стандартная
	@Override
	protected void unRegisterComponent() throws Exception {}

	@Override
	public Info getInfo() {
		return INFO;
	}

	@Override
	public ActiveComponents getActiveRoles() {
		return registerComponent;
	}

	@Override
	public void destroyed() throws Exception {

	}

	public RegisterComponent getRegisterComponent() {
		return registerComponent;
	}

}
