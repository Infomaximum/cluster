package com.infomaximum.cluster.core.io;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Формат
 * cfile:uniqueId/UUID
 */
public class URIClusterFile {

    public static String SCHEME_CLUSTER_FILE = "cfile";

    public static String createSURI(int uniqueId, String filePathUUID) {
        return new StringBuilder().append(SCHEME_CLUSTER_FILE).append(':').append(uniqueId).append('/').append(filePathUUID).toString();
    }

    public static URI createURI(int uniqueId, String fileUUID) {
        try {
            return new URI(createSURI(uniqueId, fileUUID));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getPathToComponentUniqueId(URI uri) {
        if (!URIClusterFile.SCHEME_CLUSTER_FILE.equals(uri.getScheme()))
            throw new RuntimeException("Not support scheme");
        return Integer.parseInt(uri.getSchemeSpecificPart().split("/")[0]);
    }

    public static String getPathToFileUUID(URI uri) {
        if (!URIClusterFile.SCHEME_CLUSTER_FILE.equals(uri.getScheme()))
            throw new RuntimeException("Not support scheme");
        return uri.getSchemeSpecificPart().split("/")[1];
    }

}
