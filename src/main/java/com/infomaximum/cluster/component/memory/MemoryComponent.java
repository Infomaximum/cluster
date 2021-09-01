package com.infomaximum.cluster.component.memory;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.anotation.Info;
import com.infomaximum.cluster.component.memory.core.MemoryEngine;
import com.infomaximum.cluster.struct.Component;

/**
 * Created by kris on 17.10.16.
 */
@Info(uuid = "com.infomaximum.cluster.component.memory")
public class MemoryComponent extends Component {

    private MemoryEngine memoryEngine;

    public MemoryComponent(Cluster cluster) {
        super(cluster);
        this.memoryEngine = new MemoryEngine(this);
    }

    public MemoryEngine getMemoryEngine() {
        return memoryEngine;
    }
}
