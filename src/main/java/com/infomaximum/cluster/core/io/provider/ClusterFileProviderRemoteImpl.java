package com.infomaximum.cluster.core.io.provider;

import com.infomaximum.cluster.core.io.URIClusterFile;
import com.infomaximum.cluster.core.remote.controller.clusterfile.RControllerClusterFile;
import com.infomaximum.cluster.struct.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClusterFileProviderRemoteImpl implements ClusterFileProvider {

    private final URI uri;
    private final RControllerClusterFile controllerClusterFile;

    public ClusterFileProviderRemoteImpl(Component component, URI uri) {
        this.uri = uri;
        controllerClusterFile = component.getRemotes().getFromSSKey(URIClusterFile.getPathToComponentKey(uri), RControllerClusterFile.class);
    }

    @Override
    public boolean isLocalFile() {
        return false;
    }

    @Override
    public void copyTo(Path file, CopyOption... options) throws IOException {
        InputStream inputStream = controllerClusterFile.getInputStream(URIClusterFile.getPathToFileUUID(uri));
        Files.copy(inputStream, file, options);
    }

    @Override
    public void copyTo(OutputStream target) throws IOException {
        InputStream inputStream = controllerClusterFile.getInputStream(URIClusterFile.getPathToFileUUID(uri));
        byte[] buf = new byte[8192];
        int n;
        while ((n = inputStream.read(buf)) > 0) {
            target.write(buf, 0, n);
        }
    }

    @Override
    public void delete() throws IOException {
        controllerClusterFile.delete(URIClusterFile.getPathToFileUUID(uri));
    }

    @Override
    public void deleteIfExists() throws IOException {
        controllerClusterFile.deleteIfExists(URIClusterFile.getPathToFileUUID(uri));
    }

    @Override
    public void moveTo(Path target, CopyOption... options) throws IOException {
        copyTo(target, options);
        delete();
    }

    @Override
    public long getSize() throws IOException {
        return controllerClusterFile.getSize(URIClusterFile.getPathToFileUUID(uri));
    }
}
