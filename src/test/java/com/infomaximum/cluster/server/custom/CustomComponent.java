package com.infomaximum.cluster.server.custom;

import com.infomaximum.cluster.Cluster;
import com.infomaximum.cluster.struct.Component;
import com.infomaximum.cluster.struct.Info;

/**
 * Created by kris on 12.09.17.
 */
public class CustomComponent extends Component {

    public static final Info INFO = new Info.Builder(CustomComponent.class.getPackage().getName())
            .withComponentClass(CustomComponent.class)
            .build();

    public CustomComponent(Cluster cluster) {
        super(cluster);
    }

    @Override
    public Info getInfo() {
        return INFO;
    }

}
