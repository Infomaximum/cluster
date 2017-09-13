package com.infomaximum.cluster.component.custom;

import com.infomaximum.cluster.component.memory.MemoryComponent;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.struct.config.ComponentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 12.09.17.
 */
public class CustomComponent extends Component {

    private final static Logger log = LoggerFactory.getLogger(MemoryComponent.class);

    public static final Info INFO = new Info.Builder(CustomComponent.class)
            .build();

    public CustomComponent(TransportManager transportManager, ComponentConfig config) throws Exception {
        super(transportManager, config);
    }

    @Override
    public void load() throws Exception {}

    @Override
    public ExecutorTransport initExecutorTransport() throws ReflectiveOperationException {
        return new ExecutorTransport(this);
    }

    @Override
    public Info getInfo() {
        return INFO;
    }

    @Override
    public void destroyed() throws Exception {}

}
