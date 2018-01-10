package com.infomaximum.cluster.component.memory;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.component.memory.core.MemoryEngine;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;

/**
 * Created by kris on 17.10.16.
 */
public class MemoryComponent extends Component {

    public static final Info INFO = new Info.Builder("com.infomaximum.cluster.component.memory")
            .withComponentClass(MemoryComponent.class)
            .build();

    private MemoryEngine memoryEngine;

    public MemoryComponent(Cluster cluster) {
        super(cluster);
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
