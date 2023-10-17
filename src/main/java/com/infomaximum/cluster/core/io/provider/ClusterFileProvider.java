package com.infomaximum.cluster.core.io.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Path;

public interface ClusterFileProvider {

    boolean isLocalFile();

    void copyTo(Path file, CopyOption... options) throws IOException, Exception;

    void copyTo(OutputStream target) throws IOException, Exception;

    void delete() throws IOException, Exception;

    void deleteIfExists() throws IOException, Exception;

    void moveTo(Path target, CopyOption... options) throws IOException, Exception;

    long getSize() throws IOException, Exception;

    byte[] getContent() throws IOException, Exception;
}
