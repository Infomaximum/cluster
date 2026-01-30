package com.infomaximum.cluster.component.manager.event;

import com.infomaximum.cluster.Node;
import com.infomaximum.cluster.core.component.RuntimeComponentInfo;

public interface EventUpdateComponent {

    void registerLocalComponent(RuntimeComponentInfo subSystemInfo);

    void unRegisterLocalComponent(RuntimeComponentInfo subSystemInfo);

    void registerRemoteComponent(Node node, RuntimeComponentInfo subSystemInfo);

    void startRemoteComponent(Node node, RuntimeComponentInfo subSystemInfo);

    void unRegisterRemoteComponent(Node node, RuntimeComponentInfo subSystemInfo);
}
