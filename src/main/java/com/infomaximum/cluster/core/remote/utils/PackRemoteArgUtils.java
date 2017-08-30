package com.infomaximum.cluster.core.remote.utils;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.CacheClassForName;
import com.infomaximum.rocksdb.core.struct.DomainObject;
import com.infomaximum.rocksdb.utils.ProxyDomainObjectUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by kris on 14.11.16.
 */
public class PackRemoteArgUtils {

    private final static Logger log = LoggerFactory.getLogger(ExecutorTransport.class);

    public static Object serialize(Object value) {
        try {
            if (value instanceof String || value instanceof Boolean || value instanceof Number) {
                return value;
            } else if (value instanceof JSONObject || value instanceof JSONArray) {
                return value;
            } else if (value instanceof RemoteObject) {
                return ((RemoteObject) value).serialize();
            } else if ( value instanceof Collection) {
                JSONArray out = new JSONArray();
                for (Object object : (Collection) value) {
                    JSONObject outObject = new JSONObject();
                    outObject.put("type", getClassName(object.getClass()));
                    outObject.put("value", serialize(object));
                    out.add(outObject);
                }
                return out;
            } else if (value instanceof DomainObject) {
                return ((DomainObject) value).getId();
            } else if (value instanceof Serializable) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    try(ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                        oos.writeObject(value);
                        return Base64.getEncoder().encodeToString(baos.toByteArray());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception serialize, class: " + value.getClass(), e);
        }
        throw new RuntimeException("Not support type arg: " + value.getClass());
    }

    public static Object deserialize(Component role, Class classType, Object value) throws IOException, ReflectiveOperationException, RocksDBException {
        if (classType == String.class) {
            return value;
        } else if (classType == Boolean.class || classType == boolean.class) {
            return value;
        } else if (classType == Long.class || classType == long.class) {
            return value;
        } else if (classType == Integer.class || classType == int.class) {
            return value;
        } else if (classType == Byte.class || classType == byte.class) {
            return value;
        } else if (RemoteObject.instanceOf(classType)) {
            return RemoteObject.deserialize(role, classType, (JSONObject) value);
        } else if (DomainObject.class.isAssignableFrom(classType)) {
            return role.getDomainObjectSource().get(ProxyDomainObjectUtils.getProxySuperClass(classType), (Long) value);
        } else if (JSONObject.class.isAssignableFrom(classType)) {
            return value;
        } else if (JSONArray.class.isAssignableFrom(classType)) {
            return value;
        } else if (Set.class.isAssignableFrom(classType)) {
            Set result = new HashSet();
            for (Object oItem : (JSONArray) value) {
                JSONObject item = (JSONObject) oItem;
                String type = item.getAsString("type");
                result.add(deserialize(role, CacheClassForName.get(type), item.get("value")));
            }
            return Collections.unmodifiableSet(result);
        } else if (List.class.isAssignableFrom(classType)) {
            List result = new ArrayList();
            for (Object oItem : (JSONArray) value) {
                JSONObject item = (JSONObject) oItem;
                String type = item.getAsString("type");
                result.add(deserialize(role, CacheClassForName.get(type), item.get("value")));
            }
            return Collections.unmodifiableList(result);
        } else {
            byte[] data = Base64.getDecoder().decode((String)value);
            return jDeserialize(data);
        }
    }

    private static Object jDeserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        }
    }

    public static String getClassName(Class clazz) {
        if (DomainObject.class.isAssignableFrom(clazz)) {
            if (clazz.getSuperclass() == DomainObject.class) {
                return clazz.getName();
            } else {
                return clazz.getSuperclass().getName();
            }
        } else {
            return clazz.getName();
        }
    }
}
