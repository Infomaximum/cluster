package com.infomaximum.cluster.networktransit;

import com.infomaximum.cluster.Node;

import java.util.UUID;

public class FakeNode implements Node {

    private UUID runtimeId;

    public FakeNode() {
        runtimeId = UUID.randomUUID();
    }

    @Override
    public String getName() {
        return runtimeId.toString();
    }

    @Override
    public UUID getRuntimeId() {
        return runtimeId;
    }


}
