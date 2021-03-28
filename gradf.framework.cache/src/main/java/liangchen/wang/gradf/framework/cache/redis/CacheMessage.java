package liangchen.wang.gradf.framework.cache.redis;

/**
 * @author LiangChen.Wang
 */
public class CacheMessage {
    private String name;
    private CacheAction action;
    private Object key;

    public CacheAction getAction() {
        return action;
    }

    public void setAction(CacheAction action) {
        this.action = action;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    enum CacheAction {
        //
        clear, put, evict
    }
}
