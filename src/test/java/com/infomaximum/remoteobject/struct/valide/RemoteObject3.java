package com.infomaximum.remoteobject.struct.valide;


import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;


public class RemoteObject3 implements RemoteObject {

    private final int k;
    private HashMap<String, Serializable> serializableHashMap;
    private HashMap<String, RemoteObject> remoteObjectHashMap;

    public RemoteObject3(int k) {
        this.k = k;
    }
}
