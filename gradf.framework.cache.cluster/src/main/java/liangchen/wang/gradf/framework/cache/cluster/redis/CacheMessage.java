package liangchen.wang.gradf.framework.cache.redis;

/**
 * @author LiangChen.Wang
 */
public class CacheMessage {
    private String name;
    private CacheAction action;
    private Object key;

    public static CacheMessage newInstance(String name, CacheAction action) {
        return newInstance(name, action, null);
    }

    public static CacheMessage newInstance(String name, CacheAction action, Object key) {
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.name = name;
        cacheMessage.action = action;
        cacheMessage.key = key;
        return cacheMessage;
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

    public enum CacheAction {
        //
        none, clear, put, evict
    }
}
