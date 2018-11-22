package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by kris on 14.09.16.
 */
public class Transport {

	private final TransportManager transportManager;

	private final String subSystemUuid;
	private final String subSystemKey;
	private ExecutorTransport executorTransport;

	public Transport(TransportManager transportManager, String subSystemUuid, String subSystemKey) {
		this.transportManager = transportManager;
		this.subSystemUuid = subSystemUuid;
		this.subSystemKey = subSystemKey;
	}

	public String getSubSystemUuid() {
		return subSystemUuid;
	}

	public String getSubSystemKey() {
		return subSystemKey;
	}

	public void setExecutor(ExecutorTransport executorTransport) {
		this.executorTransport = executorTransport;
	}

	public ExecutorTransport getExecutor() {
		return executorTransport;
	}

	public List<RemotePacker> getRemotePackers() {
		return transportManager.getRemotePackers();
	}

	public Object request(String targetComponentKey, Class<? extends RController> rControllerClass, Method method, Object[] args) throws Exception {
		return transportManager.transitRequest(targetComponentKey, rControllerClass.getName(), method.getName(), args);
	}
}
