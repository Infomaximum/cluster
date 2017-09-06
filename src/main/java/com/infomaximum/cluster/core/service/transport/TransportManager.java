package com.infomaximum.cluster.core.service.transport;

import com.infomaximum.cluster.core.remote.packer.RemotePacker;

import java.util.List;
import java.util.Set;

/**
 * Created by kris on 14.09.16.
 */
public interface TransportManager {

	public List<RemotePacker> getRemotePackers();

	public Transport createTransport(String subSystemUuid, String subSystemKey);

	public void destroyTransport(Transport transport);

	public void destroy();
}
