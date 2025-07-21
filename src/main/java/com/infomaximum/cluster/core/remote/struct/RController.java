package com.infomaximum.cluster.core.remote.struct;

import com.infomaximum.cluster.core.remote.RemoteTarget;

import java.util.UUID;

/**
 * Created by kris on 28.10.16.
 */
public interface RController {

    UUID getNodeRuntimeId();

    String getComponentUuid();

    RemoteTarget getRemoteTarget();
}
