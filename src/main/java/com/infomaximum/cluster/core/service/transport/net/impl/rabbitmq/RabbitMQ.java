package com.infomaximum.cluster.core.service.transport.net.impl.rabbitmq;

import com.infomaximum.cluster.core.service.transport.Transport;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.net.event.IAMQPConnect;
import com.infomaximum.cluster.core.service.transport.struct.TransportConfig;
import com.infomaximum.cluster.utils.ExecutorUtil;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by kris on 12.09.16.
 */
public class RabbitMQ implements TransportManager {

	private final static Logger log = LoggerFactory.getLogger(RabbitMQ.class);

	private final ConnectionFactory connectionFactory;
	private Connection connection;

	private List<IAMQPConnect> listenersIAMQPConnect;

	private boolean isDestroyed=false;

	public RabbitMQ(TransportConfig transportConfig) throws Exception {
		connectionFactory = new ConnectionFactory();
		connectionFactory.setUri(transportConfig.url);
		connectionFactory.setRequestedHeartbeat(60);//Detecting Dead TCP Connections

		listenersIAMQPConnect = new CopyOnWriteArrayList<IAMQPConnect>();

		//Подключаемся
		connect();
	}

	public void connect() {
		if (isDestroyed) return;

		//В отдельном потоке пытаемся подключиться к RabbitMQ
		ExecutorUtil.executors.execute(new Runnable() {
			@Override
			public void run() {

				log.debug("RabbitMQ connect...");
				try {
					connection = connectionFactory.newConnection(ExecutorUtil.executors);
					connection.addShutdownListener(new ShutdownListener() {
						public void shutdownCompleted(ShutdownSignalException cause) {
							connection.removeShutdownListener(this);

							connection = null;
							for(IAMQPConnect item: listenersIAMQPConnect) {//Оповещаем подписчиков о разединении
								try {
									item.disconnected();
								} catch (Exception e) {
									log.error("Exception fire rmq Disconnected", e);
								}
							}

							if (isDestroyed) return;
							connect();
						}
					});
					log.debug("RabbitMQ connected");

					for(IAMQPConnect item: listenersIAMQPConnect) {//Оповещаем подписчиков о соединении
						try {
							item.connected();
						} catch (Exception e) {
							log.error("Exception fire rmq connection", e);
						}
					}
				} catch (Exception e) {
					if (e.getMessage()!=null) {
						log.error("RabbitMQ connected error: " + e.getMessage());
					} else {
						log.error("RabbitMQ connected error", e);
					}
					try { Thread.sleep(3000L); } catch (InterruptedException ignore) {}
					connect();
				}

			}
		});
	}

//	@Override
//	public JSONObject request(String queueName, JSONObject request, int timeout) throws Exception {
//		try {
//			Channel channel = connection.createChannel();
//			RpcClient service = new RpcClient(channel, "", queueName, timeout);
//
//			String strResponse = service.stringCall(request.toJSONString());
//			JSONObject jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(strResponse);
//
//			channel.close();
//			return jsonObject;
//		} catch (Exception e) {
//			log.error("RPC error request: " + request, e);
//			throw e;
//		}
//	}

	@Override
	public Transport createTransport(String subSystemUuid, String subSystemKey) {
		return null;
	}

	@Override
	public void destroyTransport(Transport transport) {

	}

	@Override
	public void destroy(){
		isDestroyed=true;
		log.debug("RabbitMQ connection destroyed");
		if (connection!=null){
			try {
				connection.close();
			} catch (IOException e) {
				log.error("RabbitMQ close connected error", e);
			}
		}
	}
}
