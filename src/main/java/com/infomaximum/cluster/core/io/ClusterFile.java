package com.infomaximum.cluster.core.io;

import com.infomaximum.cluster.core.remote.controller.clusterfile.RControllerClusterFile;
import com.infomaximum.cluster.struct.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Формат
 * cfile:com.infomaximum.cluster.component.manager:00000000-0000-0000-0000-00000000000/UUID
 */
public class ClusterFile {

    private final static Logger log = LoggerFactory.getLogger(ClusterFile.class);

    private static String SCHEME_FILE = "file";

    private final Component component;
    private final URI uri;

    public ClusterFile(Component component, URI uri) {
        this.component = component;

        this.uri = uri;
        if (uri.getScheme() == null) throw new RuntimeException("Scheme is null, uri: " + uri.toString());
        if (!uri.getScheme().equals(SCHEME_FILE) && !uri.getScheme().equals(URIClusterFile.SCHEME_CLUSTER_FILE)) {
            throw new RuntimeException("Scheme is not support, uri: " + uri.toString());
        }
    }

    public URI getUri() {
        return uri;
    }

    public boolean isLocalFile() {
        return SCHEME_FILE.equals(uri.getScheme());
    }

    public void copyTo(Path file, CopyOption... options) throws IOException {
        if (isLocalFile()) {
            Files.copy(Paths.get(uri), file, options);
        } else {
            //TODO Необходима поддержка CopyOption...
            if (options.length > 0) {
                log.warn("Not implemented options!!!");
            }

            //TODO Ulitin V. Необходимо подумать как переписать на поточную обработку, сейчас будут большие накладные расходы на оперативку
            RControllerClusterFile controllerClusterFile = component.getRemotes().getFromSSKey(URIClusterFile.getPathToComponentKey(uri), RControllerClusterFile.class);
            byte[] content = controllerClusterFile.getContent(URIClusterFile.getPathToFileUUID(uri));
            Files.write(file, content);
        }
    }

    public void delete() throws IOException {
        if (isLocalFile()) {
            Files.delete(Paths.get(uri));
        } else {
            RControllerClusterFile controllerClusterFile = component.getRemotes().getFromSSKey(URIClusterFile.getPathToComponentKey(uri), RControllerClusterFile.class);
            controllerClusterFile.delete(URIClusterFile.getPathToFileUUID(uri));
        }
    }

    public void deleteIfExists() throws IOException {
        if (isLocalFile()) {
            Files.deleteIfExists(Paths.get(uri));
        } else {
            RControllerClusterFile controllerClusterFile = component.getRemotes().getFromSSKey(URIClusterFile.getPathToComponentKey(uri), RControllerClusterFile.class);
            controllerClusterFile.deleteIfExists(URIClusterFile.getPathToFileUUID(uri));
        }
    }

    public void moveTo(Path target, CopyOption... options) throws IOException {
        if (isLocalFile()) {
            Files.move(Paths.get(uri), target, options);
        } else {
            copyTo(target, options);
            delete();
        }
    }

    public long getSize() throws IOException {
        if (isLocalFile()) {
            return Files.size(Paths.get(uri));
        } else {
            RControllerClusterFile controllerClusterFile = component.getRemotes().getFromSSKey(URIClusterFile.getPathToComponentKey(uri), RControllerClusterFile.class);
            return controllerClusterFile.getSize(URIClusterFile.getPathToFileUUID(uri));
        }
    }

}
