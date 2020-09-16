package liangchen.wang.gradf.framework.springboot.event;

import liangchen.wang.gradf.framework.springboot.context.BeanLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

public enum EventPublisher {
    /**
     * instance
     */
    INSTANCE;
    private final static ApplicationContext applicationContext = BeanLoader.getContext();

    public void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }
}
