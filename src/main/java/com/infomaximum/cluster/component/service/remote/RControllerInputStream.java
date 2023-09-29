package com.infomaximum.cluster.component.service.remote;

import com.infomaximum.cluster.core.remote.struct.RController;

/**
 * Created by kris on 28.10.16.
 */
public interface RControllerInputStream extends RController {

	byte[] next(int id, int limit) throws Exception;

}
