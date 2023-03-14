package com.infomaximum.cluster.component.custom1.remote.doublebraceinitialization;

import com.infomaximum.cluster.core.remote.struct.RController;

import java.util.HashMap;

public interface RDoubleBraceInitialization extends RController {

    HashMap<String, String> failResultDoubleBraceInitialization();

    void failArgDoubleBraceInitialization(HashMap<String, String> arg);
}
