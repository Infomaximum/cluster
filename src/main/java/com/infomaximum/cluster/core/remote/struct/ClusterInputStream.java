package com.infomaximum.cluster.core.remote.struct;

import java.io.IOException;
import java.io.InputStream;

public final class ClusterInputStream extends InputStream {

    public static final int BATCH_SIZE = 1 * 1024 * 1024;//По умолчанию размер пачки в 1 Mб

    private final InputStream inputStream;

    public ClusterInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public void close() throws IOException {
        super.close();
        inputStream.close();
    }
}
