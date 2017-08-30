package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.core.service.transport.struct.packet.TPacketResponse;
import net.minidev.json.JSONObject;

import java.util.concurrent.Future;

/**
 * Created by kris on 14.09.16.
 */
public interface Transport {

	public String getSubSystemUuid();

	public String getSubSystemKey();



	public void setExecutor(ExecutorTransport executorTransport);

	public ExecutorTransport getExecutor();

	public boolean isConnected();



	public JSONObject request(String remoteControllerKey, JSONObject request) throws Exception;

	public JSONObject request(String remoteControllerKey, JSONObject request, long timeout) throws Exception;

	public Future<TPacketResponse> futureRequest(String remoteControllerKey, JSONObject request);
}
