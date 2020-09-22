package liangchen.wang.gradf.framework.cluster.configuration;

import liangchen.wang.crdf.framework.cluster.jgroups.BroadcastingCluster;

import javax.annotation.PostConstruct;

public class ClusterAutoConfigruation {
    @PostConstruct
    public void init() {
        BroadcastingCluster.INSTANCE.start();
        BroadcastingCluster.INSTANCE.broadcast("hello jgroups");
    }
}
