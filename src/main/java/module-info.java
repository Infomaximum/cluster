module com.infomaximum.cluster {
    requires java.base;
    requires org.slf4j;
    requires org.reflections;
    requires org.checkerframework.checker.qual;

    exports com.infomaximum.cluster;
    exports com.infomaximum.cluster.event;
    exports com.infomaximum.cluster.anotation;
    exports com.infomaximum.cluster.component.manager;
    exports com.infomaximum.cluster.component.manager.core;
    exports com.infomaximum.cluster.component.memory;
    exports com.infomaximum.cluster.component.memory.remote;
    exports com.infomaximum.cluster.component.memory.struct;
    exports com.infomaximum.cluster.component.service;
    exports com.infomaximum.cluster.core.component;
    exports com.infomaximum.cluster.core.io;
    exports com.infomaximum.cluster.core.remote;
    exports com.infomaximum.cluster.core.remote.controller.clusterfile;
    exports com.infomaximum.cluster.core.remote.packer;
    exports com.infomaximum.cluster.core.remote.packer.impl;
    exports com.infomaximum.cluster.core.remote.struct;
    exports com.infomaximum.cluster.core.remote.utils;
    exports com.infomaximum.cluster.core.remote.utils.validatorremoteobject;
    exports com.infomaximum.cluster.core.service.componentuuid;
    exports com.infomaximum.cluster.core.service.transport;
    exports com.infomaximum.cluster.core.service.transport.executor;
    exports com.infomaximum.cluster.core.service.transport.network;
    exports com.infomaximum.cluster.core.service.transport.network.local;
    exports com.infomaximum.cluster.core.service.transport.network.local.event;
    exports com.infomaximum.cluster.exception;
    exports com.infomaximum.cluster.struct;
    exports com.infomaximum.cluster.struct.storage;
    exports com.infomaximum.cluster.utils;
}