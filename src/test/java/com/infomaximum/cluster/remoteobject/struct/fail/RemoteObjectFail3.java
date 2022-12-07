package com.infomaximum.cluster.remoteobject.struct.fail;


import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.HashMap;
import java.util.Iterator;


public class RemoteObjectFail3 implements RemoteObject {

    private final int k;
    private HashMap<String, Iterator> iterators;

    public RemoteObjectFail3(int k) {
        this.k = k;
    }
}
