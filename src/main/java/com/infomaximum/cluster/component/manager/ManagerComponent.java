package com.infomaximum.cluster.component.manager;

import com.infomaximum.cluster.component.manager.core.RegisterComponent;
import com.infomaximum.cluster.core.component.active.ActiveComponents;
import com.infomaximum.cluster.core.component.active.ActiveComponentsImpl;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
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

	public ManagerComponent(TransportManager transportManager, ComponentConfig config) throws ReflectiveOperationException {
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
    public ExecutorTransportImpl initExecutorTransport() throws ReflectiveOperationException {
        return new ExecutorTransportImpl(this);
    }

	//Логика регистрации у менеджера подсистем не стандартная
	@Override
	protected ActiveComponentsImpl registerComponent() {
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
