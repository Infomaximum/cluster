package com.infomaximum.cluster.core.remote.struct;

import com.infomaximum.cluster.struct.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ComponentObjectOutputStream extends ObjectOutputStream {

    public final Component component;

    public ComponentObjectOutputStream(OutputStream out, Component component) throws IOException {
        super(out);
        this.component = component;
    }
}
