package com.infomaximum.cluster.server.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.anotation.Info;
import com.infomaximum.cluster.struct.Component;

/**
 * Created by kris on 12.09.17.
 */
@Info(
        uuid = "com.infomaximum.cluster.server.custom"
)
public class CustomComponent extends Component {

    public CustomComponent(Cluster cluster) {
        super(cluster);
    }

}
