package com.infomaximum.cluster.core.io;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Формат
 * cfile:com.infomaximum.cluster.component.manager:00000000-0000-0000-0000-00000000000/UUID
 */
public class URIClusterFile {

    public static String SCHEME_CLUSTER_FILE = "cfile";

    public static String createSURI(String componentKey, String filePathUUID) {
        return new StringBuilder().append(SCHEME_CLUSTER_FILE).append(':').append(componentKey).append(':').append(filePathUUID).toString();
    }

    public static URI createURI(String componentKey, String filePathUUID) {
        try {
            return new URI(createSURI(componentKey, filePathUUID));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPathToComponentKey(URI uri) {
        if (!URIClusterFile.SCHEME_CLUSTER_FILE.equals(uri.getScheme()))
            throw new RuntimeException("Not support scheme");
        return uri.getSchemeSpecificPart().split("/")[0];
    }

    public static String getPathToFileUUID(URI uri) {
        if (!URIClusterFile.SCHEME_CLUSTER_FILE.equals(uri.getScheme()))
            throw new RuntimeException("Not support scheme");
        return uri.getSchemeSpecificPart().split("/")[1];
    }

}
