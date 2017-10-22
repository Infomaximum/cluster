package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransportImpl;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by kris on 14.09.16.
 */
public interface Transport {

	public String getSubSystemUuid();

	public String getSubSystemKey();



	public void setExecutor(ExecutorTransport executorTransport);

	public ExecutorTransport getExecutor();

	public List<RemotePacker> getRemotePackers();

	public boolean isConnected();


	public Object request(String targetComponentKey, Class<? extends RController> rControllerClass, Method method, Object[] args) throws Exception;

	public Object request(String targetComponentKey, Class<? extends RController> rControllerClass, Method method, Object[] args, long timeout) throws Exception;


//	public JSONObject request(String remoteControllerKey, JSONObject request) throws Exception;
//
//	public JSONObject request(String remoteControllerKey, JSONObject request, long timeout) throws Exception;

//	public Future<TPacketResponse> futureRequest(String remoteControllerKey, JSONObject request);
}
