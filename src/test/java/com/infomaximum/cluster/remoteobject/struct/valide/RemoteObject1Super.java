package com.infomaximum.cluster.remoteobject.struct.valide;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;

public class RemoteObject1Super implements RemoteObject {

    private final int k;

    public RemoteObject1Super(int k) {
        this.k = k;
    }

    public int getK() {
        return k;
    }
}
