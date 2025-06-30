package com.infomaximum.cluster.core.remote.packer.impl;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.ComponentObjectInputStream;
import com.infomaximum.cluster.core.remote.struct.ComponentObjectOutputStream;
import com.infomaximum.cluster.exception.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerSerializable implements RemotePacker<Serializable> {

    @Override
    public boolean isSupport(Class classType) {
        return (classType.isPrimitive() || Serializable.class.isAssignableFrom(classType));
    }

    @Override
    public void validation(Type classType) {
        //К сожалению из-за слишком большого многообразия реализация - адекватную
        // проверку на этапе компиляции реализовать не удастся
    }

    @Override
    public byte[] serialize(Component component, Serializable value) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ComponentObjectOutputStream coos = new ComponentObjectOutputStream(baos, component)) {
                coos.writeObject(value);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            throw new ClusterRemotePackerException(e);
        }
    }

    @Override
    public Serializable deserialize(Component component, Class classType, byte[] value) {
        try (ComponentObjectInputStream cois = new ComponentObjectInputStream(new ByteArrayInputStream(value), component)) {
            return (Serializable) cois.readObject();
        } catch (Exception e) {
            throw new ClusterRemotePackerException("Exception deserialize, classType: " + classType + ", value: " + Arrays.toString(value), e);
        }
    }
}
