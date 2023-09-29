package com.infomaximum.cluster.core.remote.packer.impl;

import com.infomaximum.cluster.component.service.ServiceComponent;
import com.infomaximum.cluster.component.service.internal.service.ClusterInputStreamService;
import com.infomaximum.cluster.component.service.remote.RControllerInputStream;
import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.ClusterInputStream;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.ByteUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

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
            packer = new Packer(0, 0, ClusterInputStream.BATCH_SIZE, firstPacket, 0, size);
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
            packer = new Packer(serviceComponent.getUniqueId(), id, ClusterInputStream.BATCH_SIZE, firstPacket, 0, size);
        }
        return packer.serialize();
    }

    @Override
    public ClusterInputStream deserialize(Component component, Class classType, byte[] value) {
        Packer packer = Packer.deserialize(value);
        if (packer.id != 0) {
            RControllerInputStream controllerInputStream = component.getRemotes().getFromCKey(packer.sourceComponentUniqueId, RControllerInputStream.class);
            return new ClusterInputStream(new ProxyClusterInputStream(controllerInputStream, packer.id, packer.batchSize, packer.data, packer.dataOffset, packer.dataLength));
        } else {
            return new ClusterInputStream(new ByteArrayInputStream(packer.data, packer.dataOffset, packer.dataLength));
        }
    }

    public static class Packer {

        public final int sourceComponentUniqueId;
        public final int id;
        public final int batchSize;

        public final byte[] data;// Данные data - может быть больше чем нужно, т.е. реальный размер принимаем исходя из этого значение
        public final int dataOffset;
        public final int dataLength;

        public Packer(int sourceComponentUniqueId, int id, int batchSize, byte[] data, int dataOffset, int dataLength) {
            this.sourceComponentUniqueId = sourceComponentUniqueId;
            this.id = id;
            this.batchSize = batchSize;
            this.data = data;
            this.dataOffset = dataOffset;
            this.dataLength = dataLength;
        }

        public byte[] serialize() {
            byte[] result = new byte[4 + 4 + 4 + dataLength];
            ByteUtils.writeInteger(result, 0, sourceComponentUniqueId);
            ByteUtils.writeInteger(result, 4, id);
            ByteUtils.writeInteger(result, 8, batchSize);
            System.arraycopy(data, dataOffset, result, 12, dataLength);
            return result;
        }

        public static Packer deserialize(byte[] value) {
            int sourceComponentUniqueId = ByteUtils.getInteger(value, 0);
            int id = ByteUtils.getInteger(value, 4);
            int batchSize = ByteUtils.getInteger(value, 8);
            return new Packer(sourceComponentUniqueId, id, batchSize, value, 12, value.length - 12);
        }
    }

    class ProxyClusterInputStream extends InputStream {

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
