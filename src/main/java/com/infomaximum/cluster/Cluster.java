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

    private LoaderComponents loaderRoles = null;

    public Cluster(TransportManager transportManager) throws Exception {
        this.transportManager = transportManager;

        log.info("init loader subsystem...");
        loaderRoles = new LoaderComponents(transportManager);

        _instance = this;
    }

    public TransportManager getTransportManager() {
        return transportManager;
    }

    public LoaderComponents getLoaderRoles() {
        return loaderRoles;
    }

    public void destroy() {
        loaderRoles.destroy();
        transportManager.destroy();

        _instance = null;
    }

    private static Cluster _instance;
    public static Cluster getInstance(){
        return _instance;
    }
}
