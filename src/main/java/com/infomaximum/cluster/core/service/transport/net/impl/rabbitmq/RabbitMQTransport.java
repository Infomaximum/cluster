package com.infomaximum.cluster.core.service.transport.net.impl.rabbitmq;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.executor.ExecutorTransport;
import com.infomaximum.cluster.core.service.transport.net.impl.mock.MockTransportManager;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.utils.CacheClassForName;
import net.minidev.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by kris on 14.09.16.
 */
public class RabbitMQTransport implements Transport {

	private final MockTransportManager mockTransportManager;

	private final String subSystemUuid;
	private final String subSystemKey;
	private ExecutorTransport executorTransport;

	public RabbitMQTransport(MockTransportManager mockTransportManager, String subSystemUuid, String subSystemKey) {
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
	public Object request(String targetComponentKey, Class<? extends RController> rControllerClass, Method method, Object[] args) {
		return request(targetComponentKey, rControllerClass, method, args, 5L * 60L * 1000L);
	}

	@Override
	public JSONObject request(String targetComponentKey, Class<? extends RController> rControllerClass, Method method, Object[] args, long timeout) {
		throw new RuntimeException("Not implemented");
	}


	private static JSONObject packRequest(Component component, String remoteControllerName, Method method, Object[] args) throws IOException {
		JSONObject request = new JSONObject();

		request.put("controller", remoteControllerName);
		request.put("method", method.getName());

		if (args!=null) {
			JSONObject requestDataArgs = new JSONObject();
			request.put("args", requestDataArgs);
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				if (arg == null) continue;

				JSONObject requestArg = new JSONObject();
				requestArg.put("class", arg.getClass().getName());
				requestArg.put("value", component.getRemotes().getRemotePackerObjects().serialize(arg));
				requestDataArgs.put(String.valueOf(i), requestArg);
			}
		}

		return request;
	}

	private static Object unpackResponse(Component component, Method method, JSONObject response) throws ReflectiveOperationException, IOException {
		Object result = response.get("result");
		if (result==null) {
			return null;
		} else {
			String resultClass = response.getAsString("result_class");

			Class classReturnType;
			if (resultClass!=null) {
				classReturnType = CacheClassForName.get(response.getAsString("result_class"));
			} else {
				classReturnType = method.getReturnType();
			}

			return component.getRemotes().getRemotePackerObjects().deserialize(classReturnType, result);
		}
	}
}
