package com.infomaximum.cluster.remoteobject.struct.valide;


public class RemoteObject1 extends RemoteObject1Super {

    private final Long l1;

    public RemoteObject1(int k, Long l1) {
        super(k);
        this.l1 = l1;
    }

    public Long getL1() {
        return l1;
    }
}
