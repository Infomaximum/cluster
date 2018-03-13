package com.infomaximum.remoteobject.struct.valide;


import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class RemoteObject2 implements RemoteObject {

    private final int k;
    private ArrayList<Serializable> ts;

    private transient HashMap<String, Iterator> iterators;

    public RemoteObject2(int k) {
        this.k = k;
    }
}
