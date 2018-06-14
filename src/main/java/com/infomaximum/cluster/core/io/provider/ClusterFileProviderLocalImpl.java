package com.infomaximum.cluster.core.io.provider;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClusterFileProviderLocalImpl implements ClusterFileProvider {

    private final Path source;

    public ClusterFileProviderLocalImpl(Path source) {
        this.source = source;
    }

    @Override
    public boolean isLocalFile() {
        return true;
    }

    @Override
    public void copyTo(Path file, CopyOption... options) throws IOException {
        Files.copy(source, file, options);
    }

    @Override
    public void delete() throws IOException {
        Files.delete(source);
    }

    @Override
    public void deleteIfExists() throws IOException {
        Files.deleteIfExists(source);
    }

    @Override
    public void moveTo(Path target, CopyOption... options) throws IOException {
        Files.move(source, target, options);
    }

    @Override
    public long getSize() throws IOException {
        return Files.size(source);
    }
}
