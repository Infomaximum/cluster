package com.infomaximum.cluster.core.service.transport.net.queue;

/**
 * Created by kris on 13.09.16.
 */
public class QueueName {

	public static String request(String subsystem){
		return new StringBuilder().append(subsystem).append("_rpc").toString();
	}
}
