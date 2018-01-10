package com.infomaximum.cluster.component.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;

/**
 * Created by kris on 12.09.17.
 */
public class CustomComponent extends Component {

    public static final Info INFO = new Info.Builder(CustomComponent.class.getPackage().getName())
            .withComponentClass(CustomComponent.class)
            .build();

    public CustomComponent(Cluster cluster) {
        super(cluster);
    }

    @Override
    public void load() throws ClusterException {}

    @Override
    public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
        return new ExecutorTransportImpl.Builder(this).build();
    }

    @Override
    public Info getInfo() {
        return INFO;
    }

    @Override
    public void destroying() throws ClusterException {}
}
