package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.core.component.RuntimeComponentInfo;
import com.infomaximum.cluster.core.remote.struct.RemoteObject;

import java.util.ArrayList;

/**
 * Created by kris on 29.12.16.
 */
public class RegistrationState implements RemoteObject {

    public final int uniqueId;
    private ArrayList<RuntimeComponentInfo> items;

    public RegistrationState(int uniqueId, ArrayList<RuntimeComponentInfo> items) {
        this.uniqueId = uniqueId;
        this.items = items;
    }

    public ArrayList<RuntimeComponentInfo> getItems() {
        return items;
    }
}
