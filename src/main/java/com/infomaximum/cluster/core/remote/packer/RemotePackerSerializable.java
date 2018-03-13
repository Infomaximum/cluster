package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.exception.runtime.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Base64;

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
            try(ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(value);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            throw new ClusterRemotePackerException(e);
        }
    }

    @Override
    public Serializable deserialize(Component component, Class classType, byte[] value) {
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(value))) {
                return (Serializable) ois.readObject();
            }
        } catch (Exception e) {
            throw new ClusterRemotePackerException(e);
        }
    }
}
