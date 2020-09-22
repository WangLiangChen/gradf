package liangchen.wang.gradf.framework.cluster.cache;

/**
 * @author LiangChen.Wang
 */
public class CacheMessage {
    private String name;
    private CacheAction action;
    private Object key;

    public enum CacheAction {
        clear, put, evict
    }

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
}
