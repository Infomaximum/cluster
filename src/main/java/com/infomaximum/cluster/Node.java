package com.infomaximum.cluster;

import java.util.UUID;

public interface Node {

    boolean isLocal();

    String getName();

    UUID getRuntimeId();

}
