package com.infomaximum.remoteobject.struct.fail;


import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class RemoteObjectFail4 implements RemoteObject {

    private final int k;
    private HashMap<String, List<Iterator>> iterators;

    public RemoteObjectFail4(int k) {
        this.k = k;
    }
}
