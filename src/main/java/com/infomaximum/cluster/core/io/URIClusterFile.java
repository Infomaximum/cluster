package com.infomaximum.cluster.core.io;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Формат
 * cfile:nodeRuntimeId/componentId/UUID
 */
public class URIClusterFile {

    public static String SCHEME_CLUSTER_FILE = "cfile";

    public final UUID nodeRuntimeId;
    public final int componentId;
    public final String fileUUID;

    public final String uri;

    public URIClusterFile(UUID nodeRuntimeId, int componentId, String fileUUID) {
        this.nodeRuntimeId = nodeRuntimeId;
        this.componentId = componentId;
        this.fileUUID = fileUUID;

        this.uri = new StringBuilder().append(SCHEME_CLUSTER_FILE).append(':').append(nodeRuntimeId).append('/').append(componentId).append('/').append(fileUUID).toString();
    }

    public URI getURI() {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static URIClusterFile build(URI uri) {
        if (!URIClusterFile.SCHEME_CLUSTER_FILE.equals(uri.getScheme())) {
            throw new RuntimeException("Not support scheme");
        }

        String[] split = uri.getSchemeSpecificPart().split("/");
        return new URIClusterFile(UUID.fromString(split[0]), Integer.parseInt(split[1]), split[2]);
    }

}
