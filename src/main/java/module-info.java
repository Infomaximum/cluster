module com.infomaximum.cluster {
    requires java.base;
    requires org.slf4j;
    requires org.reflections;

    exports com.infomaximum.cluster.struct;
    exports com.infomaximum.cluster.core.remote.struct;
    exports com.infomaximum.cluster.core.remote;
    exports com.infomaximum.cluster.core.remote.utils.validatorremoteobject;
    exports com.infomaximum.cluster.utils;
}