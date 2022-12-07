package com.infomaximum.cluster.remoteobject.struct.fail;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.Iterator;

public class RemoteObjectFail1Super implements RemoteObject {

    private final int k;

    private final Iterator f;

    public RemoteObjectFail1Super(int k, Iterator f) {
        this.k = k;
        this.f = f;
    }
}
