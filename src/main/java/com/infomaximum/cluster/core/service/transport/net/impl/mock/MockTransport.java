package com.infomaximum.cluster.core.service.transport.net.impl.mock;

import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.core.service.transport.struct.packet.TPacketResponse;
import net.minidev.json.JSONObject;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
	public boolean isConnected() {
		return true;
	}



	@Override
	public JSONObject request(String subSystemKey, JSONObject request) throws Exception {
		return request(subSystemKey, request, 5L * 60L * 1000L);
	}

	@Override
	public JSONObject request(String subSystemKey, JSONObject request, long timeout) throws Exception {
		Future<TPacketResponse> responseFuture = futureRequest(subSystemKey, request);
		TPacketResponse packetResponse = responseFuture.get(timeout, TimeUnit.MILLISECONDS);
		if (packetResponse.getException()!=null){
			throw packetResponse.getException();
		} else {
			return packetResponse.getData();
		}
	}

	@Override
	public Future<TPacketResponse> futureRequest(String subSystemKey, JSONObject request) {
		return mockTransportManager.transitRequest(subSystemKey, request);
	}
}
