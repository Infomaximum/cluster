package com.infomaximum.cluster.core.service.transport.net.impl.mock;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by kris on 12.09.16.
 */
public class MockTransportManager implements TransportManager {

	private final Map<String, MockTransport> hashSubsystemKeyTransports;

	private final List<RemotePacker> remotePackers;

	public MockTransportManager(List<RemotePacker> remotePackers) {
		this.hashSubsystemKeyTransports = new ConcurrentHashMap<String, MockTransport>();
		this.remotePackers = Collections.unmodifiableList(remotePackers);
	}

	@Override
	public List<RemotePacker> getRemotePackers() {
		return remotePackers;
	}

	@Override
	public synchronized Transport createTransport(String subSystemUuid, String subSystemKey) {
		MockTransport transport = new MockTransport(this, subSystemUuid, subSystemKey);
		hashSubsystemKeyTransports.put(subSystemKey, transport);
		return transport;
	}

	@Override
	public synchronized void destroyTransport(Transport transport) {
		hashSubsystemKeyTransports.remove(transport.getSubSystemKey());
	}

	@Override
	public void destroy() {

	}

	protected Object transitRequest(String targetComponentKey, String rControllerClassName, String methodName, Object[] args) throws Exception {
		MockTransport targetTransport = hashSubsystemKeyTransports.get(targetComponentKey);
		if (targetTransport==null) throw new TimeoutException("Not found target component: " + targetComponentKey);

		ExecutorTransport targetExecutorTransport = targetTransport.getExecutor();
		return targetExecutorTransport.execute(rControllerClassName, methodName, args);
	}


//	public Future<TPacketResponse> transitRequest(String subSystemKey, JSONObject request) {
//		CompletableFuture<TPacketResponse> responseFuture = new CompletableFuture<TPacketResponse>();
////		ExecutorUtil.executors.execute(() -> {
//			try {
//				MockTransport transport = hashSubsystemKeyTransports.get(subSystemKey);
//				if (transport==null) throw new TimeoutException("Not found target subsystem: " + subSystemKey);
//
//				ExecutorTransport executorTransport = transport.getExecutor();
//				TPacketResponse response = executorTransport.execute(request);
//
//				responseFuture.complete(response);
//			} catch (Exception e) {
//				responseFuture.completeExceptionally(e);
//			}
//
//			//responseFuture.completeExceptionally(new RuntimeException("Not releaze"));
////		});
//		return responseFuture;
//	}
}
