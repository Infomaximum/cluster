package com.infomaximum.cluster.core.service.transport.net.event;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kris on 23.06.2015.
 */
public interface IAMQPConnect {

    public void connected() throws IOException, TimeoutException;

    public void disconnected();
}
