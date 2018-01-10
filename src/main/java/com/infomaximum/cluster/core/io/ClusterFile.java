package com.infomaximum.cluster.core.io;

import com.infomaximum.cluster.struct.Component;

import java.net.URI;
import java.nio.file.Path;

public class ClusterFile {

    private final Component component;
    private final URI uri;

    public ClusterFile(Component component, URI uri) {
        this.component = component;
        this.uri = uri;
    }


    public void copyTo(Path file) {
        //TODO Ulitin V. реализовать
        throw new RuntimeException("Not implemented");
    }

    public boolean delete() {
        //TODO Ulitin V. реализовать
        throw new RuntimeException("Not implemented");
    }

    public boolean moveTo(Path file) {
        //TODO Ulitin V. реализовать
        throw new RuntimeException("Not implemented");
    }

    public boolean isLocal() {
        //TODO Ulitin V. реализовать
        throw new RuntimeException("Not implemented");
    }
}
