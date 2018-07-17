package com.infomaximum.cluster.core.io;

import com.infomaximum.cluster.core.io.provider.ClusterFileProvider;
import com.infomaximum.cluster.core.io.provider.ClusterFileProviderLocalImpl;
import com.infomaximum.cluster.core.io.provider.ClusterFileProviderRemoteImpl;
import com.infomaximum.cluster.struct.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Формат
 * cfile:com.infomaximum.cluster.component.manager:00000000-0000-0000-0000-00000000000/UUID
 */
public class ClusterFile {

    private final static Logger log = LoggerFactory.getLogger(ClusterFile.class);

    private static String SCHEME_FILE = "file";

    private final URI uri;

    private final ClusterFileProvider clusterFileProvider;

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

    public void copyTo(Path file, CopyOption... options) throws IOException {
        clusterFileProvider.copyTo(file, options);
    }

    public void copyTo(OutputStream target) throws IOException {
        clusterFileProvider.copyTo(target);
    }

    public void delete() throws IOException {
        clusterFileProvider.delete();
    }

    public void deleteIfExists() throws IOException {
        clusterFileProvider.deleteIfExists();
    }

    public void moveTo(Path target, CopyOption... options) throws IOException {
        clusterFileProvider.moveTo(target, options);
    }

    public long getSize() throws IOException {
        return clusterFileProvider.getSize();
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
