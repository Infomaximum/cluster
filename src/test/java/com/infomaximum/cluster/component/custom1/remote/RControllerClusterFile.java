package com.infomaximum.cluster.component.custom1.remote;

import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.core.remote.struct.RController;

public interface RControllerClusterFile extends RController {

    ClusterInputStream getInputStream(int size);
}
