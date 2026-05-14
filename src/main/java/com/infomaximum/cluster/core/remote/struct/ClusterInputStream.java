package com.infomaximum.cluster.core.remote.struct;

import com.infomaximum.cluster.core.remote.packer.impl.RemotePackerClusterInputStream;
import com.infomaximum.cluster.struct.Component;

import java.io.*;

public final class ClusterInputStream extends InputStream implements Externalizable {

    @Serial
    private static final long serialVersionUID = 0L;

    public static final int BATCH_SIZE = 1 * 1024 * 1024;//По умолчанию размер пачки в 1 Mб

    private InputStream inputStream;

    @Deprecated(since = "Default no-arg constructor for Externalize")
    public ClusterInputStream() {
    }

    public ClusterInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        if (inputStream == null) {
            throw new RuntimeException("InputStream is null");
        }
        return inputStream.read();
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        if (inputStream == null) {
            throw new RuntimeException("InputStream is null");
        }
        return inputStream.readAllBytes();
    }

    @Override
    public int read(byte[] b) throws IOException {
        if (inputStream == null) {
            throw new RuntimeException("InputStream is null");
        }
        return inputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (inputStream == null) {
            throw new RuntimeException("InputStream is null");
        }
        return inputStream.read(b, off, len);
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        if (inputStream == null) {
            throw new RuntimeException("InputStream is null");
        }
        return inputStream.readNBytes(len);
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        if (inputStream == null) {
            throw new RuntimeException("InputStream is null");
        }
        return inputStream.readNBytes(b, off, len);
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        if (!(out instanceof ComponentObjectOutputStream componentObjectOutputStream)) {
            throw new RuntimeException("Invalid ObjectOutput");
        }
        Component component = componentObjectOutputStream.component;
        if (component == null) {
            throw new RuntimeException("Component in ComponentObjectOutputStream is null");
        }
        RemotePackerClusterInputStream remotePackerClusterInputStream = new RemotePackerClusterInputStream();
        out.write(remotePackerClusterInputStream.serialize(component, this));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        if (!(in instanceof ComponentObjectInputStream componentObjectInputStream)) {
            throw new RuntimeException("Invalid ObjectInput");
        }
        Component component = componentObjectInputStream.component;
        if (component == null) {
            throw new RuntimeException("Component in ComponentObjectInputStream is null");
        }
        byte[] value = componentObjectInputStream.readAllBytes();
        RemotePackerClusterInputStream.Packer packer = RemotePackerClusterInputStream.Packer.deserialize(value);
        try {
            this.inputStream = RemotePackerClusterInputStream.getInputStream(component, packer);
        } catch (IOException | ClassNotFoundException | RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
