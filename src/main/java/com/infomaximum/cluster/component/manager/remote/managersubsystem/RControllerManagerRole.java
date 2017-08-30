package com.infomaximum.cluster.component.manager.remote.managersubsystem;

import com.infomaximum.cluster.core.component.RuntimeRoleInfo;
import com.infomaximum.cluster.core.remote.struct.RController;
import com.infomaximum.cluster.struct.ComponentInfos;

/**
 * Created by kris on 02.11.16.
 */
public interface RControllerManagerRole extends RController {

	public ComponentInfos register(RuntimeRoleInfo subSystemInfo);

	public void unregister(String subSystemKey);
}


