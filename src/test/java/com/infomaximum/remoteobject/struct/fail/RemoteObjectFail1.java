package com.infomaximum.remoteobject.struct.fail;


import java.util.Iterator;

public class RemoteObjectFail1 extends RemoteObjectFail1Super {

    private final Long l1;

    public RemoteObjectFail1(int k, Iterator f, Long l1) {
        super(k, f);
        this.l1 = l1;
    }
}
