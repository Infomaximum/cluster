package com.infomaximum.cluster.core.remote.packer;

import com.infomaximum.cluster.exception.runtime.ClusterRemotePackerException;
import com.infomaximum.cluster.struct.Component;

import java.io.*;
import java.util.Base64;

/**
 * Created by user on 06.09.2017.
 */
public class RemotePackerSerializable implements RemotePacker<Serializable> {

    @Override
    public boolean isSupport(Class classType) {
        return (Serializable.class.isAssignableFrom(classType));
    }

    @Override
    public Object serialize(Component component, Serializable value) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try(ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(value);
                return Base64.getEncoder().encodeToString(baos.toByteArray());
            }
        } catch (Exception e) {
            throw new ClusterRemotePackerException(e);
        }
    }

    @Override
    public Serializable deserialize(Component component, Class classType, Object value) {
        try {
            byte[] data = Base64.getDecoder().decode((String)value);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                return (Serializable) ois.readObject();
            }
        } catch (Exception e) {
            throw new ClusterRemotePackerException(e);
        }
    }
}
