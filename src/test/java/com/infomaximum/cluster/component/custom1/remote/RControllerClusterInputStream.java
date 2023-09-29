package com.infomaximum.cluster.component.custom1.remote;

import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;

public interface RControllerClusterInputStream extends RController {

    ClusterInputStream getInputStream(int size) throws ClusterException;
}
