package com.infomaximum.cluster.core.io.provider;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;

public interface ClusterFileProvider {

    boolean isLocalFile();

    void copyTo(Path file, CopyOption... options) throws IOException;

    void delete() throws IOException;

    void deleteIfExists() throws IOException;

    void moveTo(Path target, CopyOption... options) throws IOException;

    long getSize() throws IOException;

}
