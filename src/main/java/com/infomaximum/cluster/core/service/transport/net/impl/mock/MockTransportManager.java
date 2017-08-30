package com.infomaximum.cluster.core.service.transport.net.impl.mock;

import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.core.service.transport.struct.packet.TPacketResponse;
import net.minidev.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by kris on 12.09.16.
 */
public class MockTransportManager implements TransportManager {

	private Map<String, Set<MockTransport>> transports;

	private Map<String, MockTransport> hashSubsystemKeyTransports;

	public MockTransportManager() {
		transports = new ConcurrentHashMap<String, Set<MockTransport>>();
		hashSubsystemKeyTransports = new ConcurrentHashMap<String, MockTransport>();
	}

	@Override
	public synchronized Transport createTransport(String subSystemUuid, String subSystemKey) {
		MockTransport transport = new MockTransport(this, subSystemUuid, subSystemKey);

		//Старая реализация
		Set<MockTransport> listenerTransports = transports.get(subSystemUuid);
		if (listenerTransports==null) {
			listenerTransports = new CopyOnWriteArraySet<MockTransport>();
			transports.put(subSystemUuid, listenerTransports);
		}
		listenerTransports.add(transport);

		//Новая реализация
		hashSubsystemKeyTransports.put(subSystemKey, transport);

		return transport;
	}

	@Override
	public synchronized void destroyTransport(Transport transport) {
		//Старая реализация
		Set<MockTransport> listenerTransports = transports.get(transport.getSubSystemUuid());
		if (listenerTransports==null) return;
		listenerTransports.remove(transport);
		if (listenerTransports.isEmpty()) transports.remove(transport.getSubSystemUuid());

		//Новая реализация
		hashSubsystemKeyTransports.remove(transport.getSubSystemKey());
	}

	@Override
	public void destroy() {

	}

	public Future<TPacketResponse> transitRequest(String subSystemKey, JSONObject request) {
		CompletableFuture<TPacketResponse> responseFuture = new CompletableFuture<TPacketResponse>();
//		ExecutorUtil.executors.execute(() -> {
			try {
				MockTransport transport = hashSubsystemKeyTransports.get(subSystemKey);
				if (transport==null) throw new TimeoutException("Not found target subsystem: " + subSystemKey);

				ExecutorTransport executorTransport = transport.getExecutor();
				TPacketResponse response = executorTransport.execute(request);

				responseFuture.complete(response);
			} catch (Exception e) {
				responseFuture.completeExceptionally(e);
			}

			responseFuture.completeExceptionally(new RuntimeException("Not releaze"));
//		});
		return responseFuture;
	}
}
