package com.infomaximum.cluster.struct;

import com.infomaximum.cluster.core.remote.struct.RemoteObject;

/**
 * Created by kris on 29.12.16.
 */
public class RegistrationState implements RemoteObject {

    public final int uniqueId;

    public RegistrationState(int uniqueId) {
        this.uniqueId = uniqueId;
    }
}
