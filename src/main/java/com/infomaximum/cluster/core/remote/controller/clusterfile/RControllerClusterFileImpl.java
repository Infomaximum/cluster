package com.infomaximum.cluster.core.remote.controller.clusterfile;

import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.storage.SourceClusterFile;

import java.io.IOException;

/**
 * Created by kris on 02.11.16.
 */
public class RControllerClusterFileImpl extends AbstractRController<Component> implements RControllerClusterFile {

    private final SourceClusterFile sourceClusterFile;

    public RControllerClusterFileImpl(Component component, SourceClusterFile sourceClusterFile) {
        super(component);
        this.sourceClusterFile = sourceClusterFile;
    }

    @Override
    public long getSize(String clusterFileUUID) throws IOException {
        return sourceClusterFile.getSize(clusterFileUUID);
    }

    @Override
    public byte[] getContent(String clusterFileUUID) throws IOException {
        return sourceClusterFile.getContent(clusterFileUUID);
    }

    @Override
    public void delete(String clusterFileUUID) throws IOException {
        sourceClusterFile.delete(clusterFileUUID);
    }

    @Override
    public void deleteIfExists(String clusterFileUUID) throws IOException {
        sourceClusterFile.deleteIfExists(clusterFileUUID);
    }
}


