package com.infomaximum.cluster.core.remote.packer.impl;

import com.infomaximum.cluster.component.service.ServiceComponent;
import com.infomaximum.cluster.component.service.internal.service.ClusterInputStreamService;
import com.infomaximum.cluster.component.service.remote.RControllerInputStream;
import com.infomaximum.cluster.core.remote.RemoteTarget;
import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.core.service.transport.network.LocationRuntimeComponent;
import com.infomaximum.cluster.exception.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.ByteUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerClusterInputStream implements RemotePacker<ClusterInputStream> {

    @Override
    public boolean isSupport(Class classType) {
        return ClusterInputStream.class == classType;
    }

    @Override
    public String getClassName(Class classType) {
        return ClusterInputStream.class.getName();
    }

    @Override
    public void validation(Type classType) {
    }

    @Override
    public byte[] serialize(Component component, ClusterInputStream value) {
        byte[] firstPacket = new byte[ClusterInputStream.BATCH_SIZE];
        int size;
        try {
            size = value.read(firstPacket, 0, ClusterInputStream.BATCH_SIZE);
            if (size == -1) {
                size = 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Packer packer;
        if (size < ClusterInputStream.BATCH_SIZE) {
            //размер передаваемых данных меньше размеров пакета
            packer = new Packer(new UUID(0, 0), 0, 0, ClusterInputStream.BATCH_SIZE, firstPacket, 0, size);
            try {
                value.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            //Все данные не поместились - необходима последовательная отдача данных
            ServiceComponent serviceComponent = component.getTransport().getCluster().getAnyLocalComponent(ServiceComponent.class);
            ClusterInputStreamService clusterInputStreamService = serviceComponent.clusterInputStreamService;
            int id = clusterInputStreamService.register(value);
            packer = new Packer(
                    component.getTransport().getCluster().node.getRuntimeId(),
                    serviceComponent.getId(), id, ClusterInputStream.BATCH_SIZE, firstPacket, 0, size
            );
        }
        return packer.serialize();
    }

    @Override
    public ClusterInputStream deserialize(Component component, Class classType, byte[] value) {
        Packer packer = Packer.deserialize(value);
        return new ClusterInputStream(getInputStream(component, packer));
    }

    public static InputStream getInputStream(Component component, Packer packer) {
        if (packer.id != 0) {
            LocationRuntimeComponent runtimeComponentInfo = component.getTransport().getNetworkTransit().getManagerRuntimeComponent().get(packer.sourceNodeRuntimeId, packer.sourceComponentId);
            if (runtimeComponentInfo == null) {
                throw new ClusterRemotePackerException();
            }
            RemoteTarget source = new RemoteTarget(packer.sourceNodeRuntimeId, packer.sourceComponentId, runtimeComponentInfo.component().uuid);
            RControllerInputStream controllerInputStream = component.getRemotes().getFromCKey(source, RControllerInputStream.class);
            return new ProxyClusterInputStream(controllerInputStream, packer.id, packer.batchSize, packer.data, packer.dataOffset, packer.dataLength);
        } else {
            return new ByteArrayInputStream(packer.data, packer.dataOffset, packer.dataLength);
        }
    }

    public static class Packer {

        public final UUID sourceNodeRuntimeId;
        public final int sourceComponentId;
        public final int id;
        public final int batchSize;

        public final byte[] data;// Данные data - может быть больше чем нужно, т.е. реальный размер принимаем исходя из этого значение
        public final int dataOffset;
        public final int dataLength;

        public Packer(UUID sourceNodeRuntimeId, int sourceComponentId, int id, int batchSize, byte[] data, int dataOffset, int dataLength) {
            this.sourceNodeRuntimeId = sourceNodeRuntimeId;
            this.sourceComponentId = sourceComponentId;
            this.id = id;
            this.batchSize = batchSize;
            this.data = data;
            this.dataOffset = dataOffset;
            this.dataLength = dataLength;
        }

        public byte[] serialize() {
            byte[] result = new byte[16 + 4 + 4 + 4 + dataLength];
            int offset = 0;
            offset = ByteUtils.writeLong(result, offset, sourceNodeRuntimeId.getMostSignificantBits());
            offset = ByteUtils.writeLong(result, offset, sourceNodeRuntimeId.getLeastSignificantBits());
            offset = ByteUtils.writeInteger(result, offset, sourceComponentId);
            offset = ByteUtils.writeInteger(result, offset, id);
            offset = ByteUtils.writeInteger(result, offset, batchSize);
            System.arraycopy(data, dataOffset, result, offset, dataLength);
            return result;
        }

        public static Packer deserialize(byte[] value) {
            UUID sourceNodeRuntimeId = new UUID(
                    ByteUtils.getLong(value, 0),
                    ByteUtils.getLong(value, 8)
            );
            int sourceComponentId = ByteUtils.getInteger(value, 16);
            int id = ByteUtils.getInteger(value, 20);
            int batchSize = ByteUtils.getInteger(value, 24);
            return new Packer(sourceNodeRuntimeId, sourceComponentId, id, batchSize, value, 28, value.length - 28);
        }
    }

    static class ProxyClusterInputStream extends InputStream {

        private final RControllerInputStream controllerInputStream;
        private final int id;
        private final int batchSize;

        private byte[] data;
        private int offset;
        private int length;

        private int cursor;

        public ProxyClusterInputStream(RControllerInputStream controllerInputStream, int id, int batchSize, byte[] data, int offset, int length) {
            this.controllerInputStream = controllerInputStream;
            this.id = id;
            this.batchSize = batchSize;

            this.data = data;
            this.offset = offset;
            this.length = length;

            this.cursor = 0;
        }

        @Override
        public int read() throws IOException {
            try {
                int index = cursor + offset;
                if (index >= data.length) {
                    if (index < batchSize) {
                        return -1;//Закончились данные
                    }
                    this.data = controllerInputStream.next(id, length);
                    this.offset = 0;
                    this.length = data.length;
                    if (length == 0) {
                        return -1;//Закончились данные
                    }

                    this.cursor = 0;
                    index = cursor + offset;
                }
                cursor++;

                byte b = data[index];
                return b & 0xff;//Взято из ByteArrayInputStream;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }
}
