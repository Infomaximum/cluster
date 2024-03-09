package com.infomaximum.cluster.event;

public class CauseNodeDisconnect {

    public static final CauseNodeDisconnect NORMAL = new CauseNodeDisconnect(Type.NORMAL, null);

    public enum Type {
        NORMAL,
        TIMEOUT,
        EXCEPTION
    }

    public final Type type;
    public final Throwable throwable;

    public CauseNodeDisconnect(Type type, Throwable throwable) {
        this.type = type;
        this.throwable = throwable;
    }
}
