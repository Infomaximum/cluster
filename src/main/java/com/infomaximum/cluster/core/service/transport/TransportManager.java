package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.struct.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by kris on 12.09.16.
 */
public class TransportManager {

    private final Map<Integer, Transport> componentUniqueIdTransports;

    private final List<RemotePacker> remotePackers;

    public TransportManager(List<RemotePacker> remotePackers) {
        this.componentUniqueIdTransports = new ConcurrentHashMap<Integer, Transport>();
        this.remotePackers = Collections.unmodifiableList(remotePackers);
    }

    public List<RemotePacker> getRemotePackers() {
        return remotePackers;
    }

    public Transport createTransport(Component component) {
        return new Transport(this, component);
    }

    public synchronized void registerTransport(Transport transport) {
        int uniqueId = transport.getComponent().getUniqueId();
        if (uniqueId < 0) {
            throw new RuntimeException("Internal error: Error in logic");
        }
        if (componentUniqueIdTransports.put(uniqueId, transport) != null) {
            throw new RuntimeException("Internal error: Error in logic");
        }
    }

    public synchronized void destroyTransport(Transport transport) {
        componentUniqueIdTransports.remove(transport.getComponent().getUniqueId());
    }

    public Object transitRequest(int targetComponentUniqueId, String rControllerClassName, String methodName, Object[] args) throws Exception {
        Transport targetTransport = componentUniqueIdTransports.get(targetComponentUniqueId);
        if (targetTransport == null) {
            throw new TimeoutException("Not found target component: " + targetComponentUniqueId);
        }

        ExecutorTransport targetExecutorTransport = targetTransport.getExecutor();
        return targetExecutorTransport.execute(rControllerClassName, methodName, args);
    }

    public void destroy() {

    }

}
