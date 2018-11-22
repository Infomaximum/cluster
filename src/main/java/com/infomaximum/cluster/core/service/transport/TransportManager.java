package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by kris on 12.09.16.
 */
public class TransportManager {

	private final Map<String, Transport> hashSubsystemKeyTransports;

	private final List<RemotePacker> remotePackers;

	public TransportManager(List<RemotePacker> remotePackers) {
		this.hashSubsystemKeyTransports = new ConcurrentHashMap<String, Transport>();
		this.remotePackers = Collections.unmodifiableList(remotePackers);
	}

	public List<RemotePacker> getRemotePackers() {
		return remotePackers;
	}

	public synchronized Transport createTransport(String subSystemUuid, String subSystemKey) {
		Transport transport = new Transport(this, subSystemUuid, subSystemKey);
		hashSubsystemKeyTransports.put(subSystemKey, transport);
		return transport;
	}

	public synchronized void destroyTransport(Transport transport) {
		hashSubsystemKeyTransports.remove(transport.getSubSystemKey());
	}

	public void destroy() {

	}

	public Object transitRequest(String targetComponentKey, String rControllerClassName, String methodName, Object[] args) throws Exception {
		Transport targetTransport = hashSubsystemKeyTransports.get(targetComponentKey);
		if (targetTransport==null) throw new TimeoutException("Not found target component: " + targetComponentKey);

		ExecutorTransport targetExecutorTransport = targetTransport.getExecutor();
		return targetExecutorTransport.execute(rControllerClassName, methodName, args);
	}

}
