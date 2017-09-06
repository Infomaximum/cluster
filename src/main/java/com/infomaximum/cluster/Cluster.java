package com.infomaximum.cluster;

import com.infomaximum.cluster.core.service.loader.LoaderComponents;
import com.infomaximum.cluster.core.service.transport.TransportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kris on 15.06.17.
 */
public class Cluster {

    private final static Logger log = LoggerFactory.getLogger(Cluster.class);

    private TransportManager transportManager;

    private LoaderComponents loaderComponents = null;

    public Cluster(TransportManager transportManager) throws Exception {
        this.transportManager = transportManager;

        log.info("init loader subsystem...");
        loaderComponents = new LoaderComponents(transportManager);

        _instance = this;
    }

    public TransportManager getTransportManager() {
        return transportManager;
    }

    public LoaderComponents getLoaderComponents() {
        return loaderComponents;
    }

    public void destroy() {
        loaderComponents.destroy();
        transportManager.destroy();

        _instance = null;
    }

    private static Cluster _instance;
    public static Cluster getInstance(){
        return _instance;
    }
}
