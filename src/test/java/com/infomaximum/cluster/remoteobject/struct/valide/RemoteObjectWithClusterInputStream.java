package com.infomaximum.cluster.remoteobject.struct.valide;

import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;

public record RemoteObjectWithClusterInputStream(ClusterInputStream clusterInputStream) implements RemoteObject {

}
