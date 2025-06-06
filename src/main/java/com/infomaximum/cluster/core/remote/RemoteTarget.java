package com.infomaximum.cluster.core.remote;

import java.io.Serializable;
import java.util.UUID;

public record RemoteTarget(UUID nodeRuntimeId, int componentId, String componentUuid) implements Serializable {
}
