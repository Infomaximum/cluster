package com.infomaximum.cluster.core.remote.struct;

import java.io.IOException;
import java.io.InputStream;

public final class ClusterInputStream extends InputStream {

    private final InputStream inputStream;

    public ClusterInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
}
