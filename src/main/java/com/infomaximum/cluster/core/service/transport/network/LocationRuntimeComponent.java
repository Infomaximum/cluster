package com.infomaximum.cluster.core.service.transport.network;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;

import java.util.UUID;

public record LocationRuntimeComponent(UUID node, RuntimeComponentInfo component) {
}
