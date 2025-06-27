package com.infomaximum.cluster.remoteobject.struct.valide;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class RemoteObject4<T extends RemoteObject> implements RemoteObject {

    private final T value;
    private final ArrayList<RemoteObject4<T>> children;

    public RemoteObject4(T value, @NonNull ArrayList<RemoteObject4<T>> children) {
        this.value = value;
        this.children = children;
    }

    public T getValue() {
        return value;
    }

    public ArrayList<RemoteObject4<T>> getChildren() {
        return children;
    }

    private record RValue(long value) implements RemoteObject {

    }
}
