package liangchen.wang.gradf.framework.cluster.configuration;


import liangchen.wang.gradf.framework.cluster.jgroups.BroadcastingCluster;

import javax.annotation.PostConstruct;
/**
 * @author LiangChen.Wang
 */
public class ClusterAutoConfigruation {
    @PostConstruct
    public void init() {
        BroadcastingCluster.INSTANCE.start();
        BroadcastingCluster.INSTANCE.broadcast("hello jgroups");
    }
}
