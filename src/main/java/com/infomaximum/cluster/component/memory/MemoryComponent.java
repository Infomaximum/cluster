package com.infomaximum.cluster.component.memory;

import com.infomaximum.cluster.component.memory.core.MemoryEngine;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.struct.config.ComponentConfig;
import com.infomaximum.cluster.utils.version.AppVersion;

/**
 * Created by kris on 17.10.16.
 */
public class MemoryComponent extends Component {

    public static final Info INFO = new Info.Builder(MemoryComponent.class)
            .withEnvironmentVersion(AppVersion.getVersion(MemoryComponent.class))
            .build();

    private MemoryEngine memoryEngine;

    public MemoryComponent(ComponentConfig config) throws Exception {
        super(config);
    }

    @Override
    public void load() throws ClusterException {
        this.memoryEngine = new MemoryEngine(this);
    }

    @Override
    public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
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
    public void destroying() throws ClusterException {
        // do nothing
    }
}
