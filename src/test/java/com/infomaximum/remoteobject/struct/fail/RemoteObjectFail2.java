package com.infomaximum.remoteobject.struct.fail;


import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.Iterator;
import java.util.List;


public class RemoteObjectFail2 implements RemoteObject {

    private final int k;
    private List<Iterator> iterators;

    public RemoteObjectFail2(int k) {
        this.k = k;
    }
}
