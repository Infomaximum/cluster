package com.infomaximum.cluster.component.custom1.remote.doublebraceinitialization;

import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.exception.ClusterException;

import java.util.HashMap;

public interface RDoubleBraceInitialization extends RController {

    HashMap<String, String> failResultDoubleBraceInitialization() throws ClusterException;

    void failArgDoubleBraceInitialization(HashMap<String, String> arg) throws ClusterException;
}
