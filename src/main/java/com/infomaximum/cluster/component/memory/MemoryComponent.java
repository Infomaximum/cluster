package com.infomaximum.cluster.component.memory;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.anotation.Info;
import com.infomaximum.cluster.component.memory.core.MemoryEngine;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.struct.Component;

/**
 * Created by kris on 17.10.16.
 */
@Info(uuid = "com.infomaximum.cluster.component.memory")
public class MemoryComponent extends Component {

    private final MemoryEngine memoryEngine;

    public MemoryComponent() {
        this.memoryEngine = new MemoryEngine(this);
    }

    @Override
    public void init(Cluster cluster, TransportManager transportManager) {
        super.init(cluster, transportManager);
        start();
    }

    public MemoryEngine getMemoryEngine() {
        return memoryEngine;
    }
}
