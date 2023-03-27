package com.infomaximum.cluster.utils;

import java.time.Duration;

public class ExpireObject<T> {

    private final T object;
    private long timeUpdate;

    public ExpireObject(T object) {
        this.object = object;
        this.timeUpdate = System.currentTimeMillis();
    }

    public T get() {
        this.timeUpdate = System.currentTimeMillis();
        return object;
    }

    public boolean isExpire(Duration expire) {
        return ((System.currentTimeMillis() - timeUpdate) > expire.toMillis());
    }
}
