package com.infomaximum.cluster.core.service.transport.network.local.event;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;

public interface EventUpdateLocalComponent {

    void registerComponent(RuntimeComponentInfo subSystemInfo);

    void unRegisterComponent(RuntimeComponentInfo subSystemInfo);
}
