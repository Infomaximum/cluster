module com.infomaximum.cluster {
    requires java.base;
    requires org.slf4j;
    requires org.reflections;

    exports com.infomaximum.cluster.struct;
    exports com.infomaximum.cluster.core.remote.struct;
    exports com.infomaximum.cluster.core.remote;
    exports com.infomaximum.cluster.core.remote.utils.validatorremoteobject;
    exports com.infomaximum.cluster.utils;
    exports com.infomaximum.cluster;
    exports com.infomaximum.cluster.core.service.transport.executor;
    exports com.infomaximum.cluster.exception;
    exports com.infomaximum.cluster.core.remote.utils;
    exports com.infomaximum.cluster.anotation;
    exports com.infomaximum.cluster.core.remote.controller.clusterfile;
    exports com.infomaximum.cluster.core.io;
    exports com.infomaximum.cluster.struct.storage;
    exports com.infomaximum.cluster.core.remote.packer;
}