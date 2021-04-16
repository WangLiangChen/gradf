package liangchen.wang.gradf.framework.cache.cluster.enumeration;

/**
 * @author LiangChen.Wang
 */
public enum CacheStatus {
    //
    INSTANCE;
    private boolean redisEnable;

    public boolean isRedisEnable() {
        return redisEnable;
    }

    public boolean isNotRedisEnable() {
        return !redisEnable;
    }

    public void setRedisEnable(boolean redisEnable) {
        this.redisEnable = redisEnable;
    }
}
