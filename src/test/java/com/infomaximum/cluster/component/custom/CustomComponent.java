package com.infomaximum.cluster.component.custom;

import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;
import com.infomaximum.cluster.utils.version.AppVersion;

/**
 * Created by kris on 12.09.17.
 */
public class CustomComponent extends Component {

    public static final Info INFO = new Info.Builder(CustomComponent.class)
            .withEnvironmentVersion(AppVersion.getVersion(CustomComponent.class))
            .build();

    @Override
    public void load() throws ClusterException {}

    @Override
    public ExecutorTransportImpl initExecutorTransport() throws ClusterException {
        return new ExecutorTransportImpl(this);
    }

    @Override
    public Info getInfo() {
        return INFO;
    }

    @Override
    public void destroying() throws ClusterException {}
}
