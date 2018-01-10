package com.infomaximum.cluster.core.remote.controller.clusterfile;

import com.infomaximum.cluster.core.remote.struct.RController;

import java.io.IOException;

/**
 * Created by kris on 02.11.16.
 */
public interface RControllerClusterFile extends RController {

    long getSize(String clusterFileUUID) throws IOException;

    byte[] getContent(String clusterFileUUID) throws IOException;

    void delete(String clusterFileUUID) throws IOException;

    void deleteIfExists(String clusterFileUUID) throws IOException;
}


