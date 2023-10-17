package com.infomaximum.cluster.core.io;

import com.infomaximum.cluster.core.io.provider.ClusterFileProvider;
import com.infomaximum.cluster.core.io.provider.ClusterFileProviderLocalImpl;
import com.infomaximum.cluster.core.io.provider.ClusterFileProviderRemoteImpl;
import com.infomaximum.cluster.struct.Component;

import java.io.OutputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Формат
 * cfile:uniqueId/UUID
 */
public class ClusterFile {

    private static String SCHEME_FILE = "file";

    private final URI uri;

    protected final ClusterFileProvider clusterFileProvider;

    public ClusterFile(Component component, URI uri) {
        this.uri = uri;
        if (uri.getScheme() == null) throw new IllegalArgumentException("Scheme is null, uri: " + uri.toString());

        clusterFileProvider = provider(component, uri);
    }

    public URI getUri() {
        return uri;
    }

    public boolean isLocalFile() {
        return SCHEME_FILE.equals(uri.getScheme());
    }

    public void copyTo(Path file, CopyOption... options) throws Exception {
        clusterFileProvider.copyTo(file, options);
    }

    public void copyTo(OutputStream target) throws Exception {
        clusterFileProvider.copyTo(target);
    }

    public void delete() throws Exception {
        clusterFileProvider.delete();
    }

    public void deleteIfExists() throws Exception {
        clusterFileProvider.deleteIfExists();
    }

    public void moveTo(Path target, CopyOption... options) throws Exception {
        clusterFileProvider.moveTo(target, options);
    }

    public long getSize() throws Exception {
        return clusterFileProvider.getSize();
    }

    public byte[] getContent() throws Exception {
        return clusterFileProvider.getContent();
    }

    private static ClusterFileProvider provider(Component component, URI source) {
        if (source.getScheme().equals(SCHEME_FILE)) {
            return new ClusterFileProviderLocalImpl(Paths.get(source));
        } else if (source.getScheme().equals(URIClusterFile.SCHEME_CLUSTER_FILE)) {
            return new ClusterFileProviderRemoteImpl(component, source);
        } else {
            throw new RuntimeException("Scheme is not support, uri: " + source.toString());
        }
    }
}
