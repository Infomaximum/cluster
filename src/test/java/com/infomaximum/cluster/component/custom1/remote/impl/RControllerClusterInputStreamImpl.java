package com.infomaximum.cluster.component.custom1.remote.impl;

import com.infomaximum.cluster.component.custom1.Custom1Component;
import com.infomaximum.cluster.component.custom1.remote.RControllerClusterInputStream;
import com.infomaximum.cluster.core.remote.AbstractRController;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.utils.ChainBytesUtils;

import java.io.InputStream;

public class RControllerClusterInputStreamImpl extends AbstractRController<Custom1Component> implements RControllerClusterInputStream {

    public RControllerClusterInputStreamImpl(Custom1Component component) {
        super(component);
    }

    @Override
    public ClusterInputStream getInputStream(int size) {
        final int[] count = {0};
        return new ClusterInputStream(new InputStream() {
            @Override
            public int read() {
                if (count[0] >= size) {
                    return -1;
                }
                byte b = ChainBytesUtils.get(count[0]++);
                return b & 0xff;//Взято из ByteArrayInputStream
            }
        });
    }
}
