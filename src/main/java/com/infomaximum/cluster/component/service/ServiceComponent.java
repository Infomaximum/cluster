package com.infomaximum.cluster.component.service;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.anotation.Info;
import com.infomaximum.cluster.component.service.internal.service.ClusterInputStreamService;
import com.infomaximum.cluster.struct.Component;

@Info(uuid = "com.infomaximum.cluster.component.service")
public class ServiceComponent extends Component {

    public final ClusterInputStreamService clusterInputStreamService;

    public ServiceComponent(Cluster cluster) {
        super(cluster);

        this.clusterInputStreamService = new ClusterInputStreamService();
    }

}
