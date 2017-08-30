package com.infomaximum.cluster.core.service.transport;

/**
 * Created by kris on 14.09.16.
 */
public interface TransportManager {

	public Transport createTransport(String subSystemUuid, String subSystemKey);

	public void destroyTransport(Transport transport);

	public void destroy();
}
