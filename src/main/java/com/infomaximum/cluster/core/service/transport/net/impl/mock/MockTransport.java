package com.infomaximum.cluster.core.service.transport.net.impl.mock;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by kris on 14.09.16.
 */
public class MockTransport implements Transport {

	private final MockTransportManager mockTransportManager;

	private final String subSystemUuid;
	private final String subSystemKey;
	private ExecutorTransport executorTransport;

	public MockTransport(MockTransportManager mockTransportManager, String subSystemUuid, String subSystemKey) {
		this.mockTransportManager = mockTransportManager;
		this.subSystemUuid = subSystemUuid;
		this.subSystemKey = subSystemKey;
	}

	@Override
	public String getSubSystemUuid() {
		return subSystemUuid;
	}

	public String getSubSystemKey() {
		return subSystemKey;
	}

	@Override
	public void setExecutor(ExecutorTransport executorTransport) {
		this.executorTransport = executorTransport;
	}

	@Override
	public ExecutorTransport getExecutor() {
		return executorTransport;
	}

	@Override
	public List<RemotePacker> getRemotePackers() {
		return mockTransportManager.getRemotePackers();
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public Object request(String targetComponentKey, Class<? extends RController> rControllerClass, Method method, Object[] args) throws Exception {
		return request(targetComponentKey, rControllerClass, method, args, 5L * 60L * 1000L);
	}

	@Override
	public Object request(String targetComponentKey, Class<? extends RController> rControllerClass, Method method, Object[] args, long timeout) throws Exception {
		return mockTransportManager.transitRequest(targetComponentKey, rControllerClass.getName(), method.getName(), args);
	}

}
