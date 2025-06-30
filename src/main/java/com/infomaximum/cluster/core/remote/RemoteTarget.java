package com.infomaximum.cluster.core.remote;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.UUID;

public record RemoteTarget(UUID nodeRuntimeId, int componentId, String componentUuid) implements RemoteObject {
}
