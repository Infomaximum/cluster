package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.NetworkTransit;
import com.infomaximum.cluster.core.remote.packer.RemotePackerObject;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.Method;

/**
 * Created by kris on 14.09.16.
 */
public class LocalTransport {

	private final TransportManager transportManager;

	private final Component component;
	private ExecutorTransport executorTransport;

	public LocalTransport(TransportManager transportManager, Component component) {
		this.transportManager = transportManager;
		this.component = component;
	}

	public NetworkTransit getNetworkTransit() {
		return transportManager.networkTransit;
	}

	public Component getComponent() {
		return component;
	}

	public void setExecutor(ExecutorTransport executorTransport) {
		this.executorTransport = executorTransport;
	}

	public ExecutorTransport getExecutor() {
		return executorTransport;
	}

	public RemotePackerObject getRemotePackerObject() {
		return transportManager.getRemotePackerObject();
	}

	public Object request(int targetComponentUniqueId, Class<? extends RController> rControllerClass, Method method, Object[] args) throws Exception {
		return transportManager.request(component, targetComponentUniqueId, rControllerClass.getName(), method, args);
	}
}
