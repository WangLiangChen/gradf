package liangchen.wang.gradf.framework.cache.primary;

/**
 * @author LiangChen.Wang
 * ttl=3600,tti=300,表示有效时间3600s，如果连续300s未访问
 */
public class DefaultCacheProperties {
    private String md5Name;
    private String originalName;
    // time to live ,自创建至失效的时间间隔
    private Long ttl;
    // time to idel,自创建后，最后一次访问至失效的时间间隔
    private Long tti;


    public String getMd5Name() {
        return md5Name;
    }

    public void setMd5Name(String md5Name) {
        this.md5Name = md5Name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public Long getTti() {
        return tti;
    }

    public void setTti(Long tti) {
        this.tti = tti;
    }
}
