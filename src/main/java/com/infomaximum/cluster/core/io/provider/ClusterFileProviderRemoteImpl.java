package com.infomaximum.cluster.core.io.provider;

import com.infomaximum.cluster.core.io.URIClusterFile;
import com.infomaximum.cluster.core.remote.RemoteTarget;
import com.infomaximum.cluster.core.remote.controller.clusterfile.RControllerClusterFile;
import com.infomaximum.cluster.core.service.transport.network.LocationRuntimeComponent;
import com.infomaximum.cluster.exception.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClusterFileProviderRemoteImpl implements ClusterFileProvider {

    private final URIClusterFile clusterFile;
    private final RControllerClusterFile controllerClusterFile;

    public ClusterFileProviderRemoteImpl(Component component, URI uri) {
        this.clusterFile = URIClusterFile.build(uri);

        LocationRuntimeComponent runtimeComponentInfo = component.getTransport().getNetworkTransit().getManagerRuntimeComponent().get(clusterFile.nodeRuntimeId, clusterFile.componentId);
        if (runtimeComponentInfo == null) {
            throw new ClusterRemotePackerException();
        }
        RemoteTarget target = new RemoteTarget(clusterFile.nodeRuntimeId, clusterFile.componentId, runtimeComponentInfo.component().uuid);

        controllerClusterFile = component.getRemotes().getFromCKey(target, RControllerClusterFile.class);
    }

    @Override
    public boolean isLocalFile() {
        return false;
    }

    @Override
    public void copyTo(Path file, CopyOption... options) throws Exception {
        try (InputStream inputStream = controllerClusterFile.getInputStream(clusterFile.fileUUID)) {
            Files.copy(inputStream, file, options);
        }
    }

    @Override
    public void copyTo(OutputStream target) throws Exception {
        try (InputStream inputStream = controllerClusterFile.getInputStream(clusterFile.fileUUID)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = inputStream.read(buf)) > 0) {
                target.write(buf, 0, n);
            }
        }
    }

    @Override
    public void delete() throws Exception {
        controllerClusterFile.delete(clusterFile.fileUUID);
    }

    @Override
    public void deleteIfExists() throws Exception {
        controllerClusterFile.deleteIfExists(clusterFile.fileUUID);
    }

    @Override
    public void moveTo(Path target, CopyOption... options) throws Exception {
        copyTo(target, options);
        delete();
    }

    @Override
    public long getSize() throws Exception {
        return controllerClusterFile.getSize(clusterFile.fileUUID);
    }

    @Override
    public byte[] getContent() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream inputStream = controllerClusterFile.getInputStream(clusterFile.fileUUID)) {
            int nRead;
            byte[] data = new byte[2048];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        }
        return buffer.toByteArray();
    }
}
