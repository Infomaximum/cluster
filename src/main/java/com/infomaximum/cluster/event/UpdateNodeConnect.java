package com.infomaximum.cluster.event;

import com.infomaximum.cluster.Node;

public interface UpdateNodeConnect {

    void onConnect(Node node);

    void onDisconnect(Node node, CauseNodeDisconnect cause);

}
