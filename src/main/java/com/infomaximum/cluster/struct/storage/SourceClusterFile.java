package com.infomaximum.cluster.struct.storage;

import java.io.IOException;

public interface SourceClusterFile {

    long getSize(String clusterFileUUID) throws IOException;

    byte[] getContent(String clusterFileUUID) throws IOException;

    void delete(String clusterFileUUID) throws IOException;

    void deleteIfExists(String clusterFileUUID) throws IOException;

}
