package com.infomaximum.cluster.builder.transport;

import com.infomaximum.cluster.core.service.transport.TransportManager;
import com.infomaximum.cluster.core.service.transport.net.impl.mock.MockTransportManager;

/**
 * Created by kris on 15.06.17.
 */
public class RabbitMQTransportBuilder extends TransportBuilder {

    @Override
    public TransportManager build() {
        throw new RuntimeException();
    }
}
