package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.NetworkTransit;
import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.packer.RemotePackerObject;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.executor.ComponentExecutorTransport;
import com.infomaximum.cluster.exception.ExceptionBuilder;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.GlobalUniqueIdUtils;
import com.infomaximum.cluster.utils.MethodKey;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kris on 12.09.16.
 */
public class TransportManager {

    private final Cluster cluster;

    private final RemotePackerObject remotePackerObject;

    public final NetworkTransit networkTransit;

    private final Map<Integer, LocalTransport> localComponentUniqueIdTransports;

    private final ExceptionBuilder exceptionBuilder;

    public TransportManager(Cluster cluster, NetworkTransit.Builder builderNetworkTransit, List<RemotePacker> remotePackers, ExceptionBuilder exceptionBuilder) {
        this.cluster = cluster;
        this.remotePackerObject = new RemotePackerObject(remotePackers);
        this.networkTransit = builderNetworkTransit.build(this);

        this.localComponentUniqueIdTransports = new ConcurrentHashMap<Integer, LocalTransport>();

        this.exceptionBuilder = exceptionBuilder;
    }

    public RemotePackerObject getRemotePackerObject() {
        return remotePackerObject;
    }

    public LocalTransport createTransport(Component component) {
        return new LocalTransport(this, component);
    }

    public ExceptionBuilder getExceptionBuilder() {
        return exceptionBuilder;
    }

    public synchronized void registerTransport(LocalTransport transport) {
        int uniqueId = transport.getComponent().getUniqueId();
        if (uniqueId < 0) {
            throw new RuntimeException("Internal error: Error in logic");
        }
        if (localComponentUniqueIdTransports.put(uniqueId, transport) != null) {
            throw new RuntimeException("Internal error: Error in logic");
        }
    }

    public synchronized void destroyTransport(LocalTransport transport) {
        localComponentUniqueIdTransports.remove(transport.getComponent().getUniqueId());
    }

    public Object request(Component sourceComponent, int targetComponentUniqueId, Class<? extends RController> rControllerClass, Method method, Object[] args) throws Throwable {
        byte targetNode = GlobalUniqueIdUtils.getNode(targetComponentUniqueId);
        if (targetNode == networkTransit.getNode()) {
            //локальный запрос
            ComponentExecutorTransport targetComponentExecutorTransport = getLocalExecutorTransport(targetComponentUniqueId);
            return targetComponentExecutorTransport.execute(rControllerClass.getName(), method, args);
        } else {
            //сетевой запрос
            int methodKey = MethodKey.calcMethodKey(method);
            byte[][] sargs;
            if (args == null) {
                sargs = null;
            } else {
                sargs = new byte[args.length][];
                Class[] parameterTypes = method.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    sargs[i] = remotePackerObject.serialize(sourceComponent, parameterTypes[i], args[i]);
                }
            }
            ComponentExecutorTransport.Result result = networkTransit.getRemoteControllerRequest().request(sourceComponent, targetComponentUniqueId, rControllerClass.getName(), methodKey, sargs);
            if (result.exception() != null) {
                throw (Throwable) remotePackerObject.deserialize(sourceComponent, Throwable.class, result.exception());
            } else {
                return remotePackerObject.deserialize(sourceComponent, method.getReturnType(), result.value());
            }
        }
    }

    public ComponentExecutorTransport.Result localRequest(int targetComponentUniqueId, String rControllerClassName, int methodKey, byte[][] byteArgs) throws Exception {
        ComponentExecutorTransport targetComponentExecutorTransport = getLocalExecutorTransport(targetComponentUniqueId);
        return targetComponentExecutorTransport.execute(rControllerClassName, methodKey, byteArgs);
    }

    private ComponentExecutorTransport getLocalExecutorTransport(int targetComponentUniqueId) throws Exception {
        LocalTransport targetTransport = localComponentUniqueIdTransports.get(targetComponentUniqueId);
        if (targetTransport == null) {
            throw cluster.getExceptionBuilder().buildRemoteComponentNotFoundException(
                    GlobalUniqueIdUtils.getNode(targetComponentUniqueId), targetComponentUniqueId
            );
        } else {
            return targetTransport.getExecutor();
        }
    }

    public void destroy() {
        networkTransit.close();
    }
}