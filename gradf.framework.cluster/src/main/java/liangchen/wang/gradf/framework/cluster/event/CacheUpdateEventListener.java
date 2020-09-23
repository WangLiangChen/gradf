package liangchen.wang.gradf.framework.cluster.event;

import liangchen.wang.gradf.framework.cache.event.CacheUpdateEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 模块解耦使用Event，收到本地缓存的事件后，发送jgroup广播
 *
 * @author LiangChen.Wang 2020/9/23
 */
@Component
public class CacheUpdateEventListener implements ApplicationListener<CacheUpdateEvent> {
    @Override
    public void onApplicationEvent(CacheUpdateEvent cacheUpdateEvent) {

    }
}
