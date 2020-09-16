package liangchen.wang.gradf.framework.web.sse;


import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author LiangChen.Wang
 */
@Component
public class SseEventListener implements ApplicationListener<SseEvent> {
    @Override
    public void onApplicationEvent(SseEvent sseEvent) {
        if (sseEvent instanceof SseUnicastEvent) {
            SseUnicastEvent sseUnicastEvent = ClassBeanUtil.INSTANCE.cast(sseEvent);
            SseUtil.INSTANCE.unicast(sseUnicastEvent.getSseKey(), sseEvent.getMessage());
            return;
        }
        if (sseEvent instanceof SseMulticastEvent) {
            SseMulticastEvent sseMulticastEvent = ClassBeanUtil.INSTANCE.cast(sseEvent);
            SseUtil.INSTANCE.multicast(sseMulticastEvent.getGroup(), sseEvent.getMessage());
            return;
        }
        SseUtil.INSTANCE.broadcast(sseEvent.getMessage());
    }
}
