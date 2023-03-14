package com.infomaximum.cluster.component.custom1;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.anotation.Info;
import com.infomaximum.cluster.struct.Component;

/**
 * Created by kris on 12.09.17.
 */
@Info(
        uuid = "com.infomaximum.cluster.component.custom1"
)
public class Custom1Component extends Component {

    public Custom1Component(Cluster cluster) {
        super(cluster);
    }

}
