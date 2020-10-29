package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.struct.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by kris on 14.09.16.
 */
public class Transport {

	private final TransportManager transportManager;

	private final Component component;
	private ExecutorTransport executorTransport;

	public Transport(TransportManager transportManager, Component component) {
		this.transportManager = transportManager;
		this.component = component;
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

	public List<RemotePacker> getRemotePackers() {
		return transportManager.getRemotePackers();
	}

	public Object request(int targetComponentUniqueId, Class<? extends RController> rControllerClass, Method method, Object[] args) throws Exception {
		return transportManager.transitRequest(targetComponentUniqueId, rControllerClass.getName(), method.getName(), args);
	}
}
