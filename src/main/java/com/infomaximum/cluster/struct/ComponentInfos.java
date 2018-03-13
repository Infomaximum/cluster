package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.ArrayList;

/**
 * Created by kris on 29.12.16.
 */
public class ComponentInfos implements RemoteObject {

    private ArrayList<RuntimeComponentInfo> items;

    public ComponentInfos(ArrayList<RuntimeComponentInfo> items) {
        this.items = items;
    }

    public ArrayList<RuntimeComponentInfo> getItems() {
        return items;
    }
}
