package com.infomaximum.cluster.component.service.internal.service;

import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.exception.ClusterException;
import com.infomaximum.cluster.utils.ExpireObject;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClusterInputStreamService {

    private static final Duration DURATION_AUTO_REMOVE = Duration.ofHours(1);
    private final AtomicInteger ids;
    private final ConcurrentHashMap<Integer, ExpireObject<ClusterInputStream>> inputStreams;

    private final Duration durationAutoRemove;

    public ClusterInputStreamService() {
        this(DURATION_AUTO_REMOVE);
    }

    public ClusterInputStreamService(Duration durationAutoRemove) {
        this.ids = new AtomicInteger(0);
        this.inputStreams = new ConcurrentHashMap<>();
        this.durationAutoRemove = durationAutoRemove;
    }

    public int register(ClusterInputStream clusterInputStream) {
        int id = getNextId();
        if (inputStreams.putIfAbsent(id, new ExpireObject(clusterInputStream)) != null) {
            throw new RuntimeException("error in logic");
        }
        garbageCollection();
        return id;
    }

    public byte[] read(int id, int limit) {
        ExpireObject<ClusterInputStream> eInputStream = inputStreams.get(id);
        if (eInputStream == null) {
            throw new ClusterException("Not found clusterInputStream: " + id);
        }
        ClusterInputStream inputStream = eInputStream.get();

        byte[] source = new byte[limit];
        int size;
        try {
            size = inputStream.read(source, 0, source.length);
            if (size == -1) {
                size = 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (size < limit) {//Данные закончились, сразу закрываем стрим и чистим
            remove(id);
        }

        byte[] result = new byte[size];
        if (size > 0) {
            System.arraycopy(source, 0, result, 0, size);
        }
        return result;
    }

    private int getNextId() {
        while (true) {
            int id = ids.incrementAndGet();
            if (id >= Integer.MAX_VALUE - 1) {
                ids.set(0);
            }
            if (!inputStreams.contains(id)) {
                return id;
            }
        }
    }

    private void garbageCollection() {
        for (Map.Entry<Integer, ExpireObject<ClusterInputStream>> entry : inputStreams.entrySet()) {
            if (entry.getValue().isExpire(durationAutoRemove)) {
                remove(entry.getKey());
            }
        }
    }

    private void remove(int id) {
        ExpireObject<ClusterInputStream> eInputStream = inputStreams.remove(id);
        if (eInputStream != null) {
            try {
                eInputStream.get().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
