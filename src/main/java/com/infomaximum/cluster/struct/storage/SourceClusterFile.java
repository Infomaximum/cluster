package com.infomaximum.cluster.struct.storage;

import java.io.IOException;
import java.io.InputStream;

public interface SourceClusterFile {

    boolean contains(String clusterFileUUID) throws IOException;

    long getSize(String clusterFileUUID) throws IOException;

    InputStream getInputStream(String clusterFileUUID) throws IOException;

    void delete(String clusterFileUUID) throws IOException;

    void deleteIfExists(String clusterFileUUID) throws IOException;

}
