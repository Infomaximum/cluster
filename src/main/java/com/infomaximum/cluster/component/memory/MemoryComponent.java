package com.infomaximum.cluster.component.memory;

import com.infomaximum.cluster.component.memory.core.MemoryEngine;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.struct.config.ComponentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 17.10.16.
 */
public class MemoryComponent extends Component {

	private final static Logger log = LoggerFactory.getLogger(MemoryComponent.class);

	public static final Info INFO = new Info.Builder(MemoryComponent.class)
			.build();

	private MemoryEngine memoryEngine;

	public MemoryComponent(TransportManager transportManager, ComponentConfig config) throws Exception {
		super(transportManager, config);
	}

	@Override
	public void load() throws Exception {
		this.memoryEngine = new MemoryEngine(this);
	}

	@Override
    public ExecutorTransportImpl initExecutorTransport() throws ReflectiveOperationException {
        return new ExecutorTransportImpl(this);
    }

	@Override
	public Info getInfo() {
		return INFO;
	}

	public MemoryEngine getMemoryEngine() {
		return memoryEngine;
	}

	@Override
	public void destroyed() throws Exception {

	}
}
