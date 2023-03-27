package com.infomaximum.cluster.component.service.remote.impl;

import com.infomaximum.cluster.component.service.ServiceComponent;
import com.infomaximum.cluster.component.service.internal.service.ClusterInputStreamService;
import com.infomaximum.cluster.component.service.remote.RControllerInputStream;
import com.infomaximum.cluster.core.remote.AbstractRController;

public class RControllerInputStreamImpl extends AbstractRController<ServiceComponent> implements RControllerInputStream {

    private final ClusterInputStreamService clusterInputStreamService;

    public RControllerInputStreamImpl(ServiceComponent component) {
        super(component);
        this.clusterInputStreamService = component.clusterInputStreamService;
    }

    @Override
    public byte[] next(int id, int limit) {
        return clusterInputStreamService.read(id, limit);
    }
}
