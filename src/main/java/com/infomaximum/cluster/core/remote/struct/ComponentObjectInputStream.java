package com.infomaximum.cluster.core.remote.struct;

import com.infomaximum.cluster.struct.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ComponentObjectInputStream extends ObjectInputStream {

    public final Component component;

    public ComponentObjectInputStream(InputStream in, Component component) throws IOException {
        super(in);
        this.component = component;
    }
}
