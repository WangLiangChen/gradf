package liangchen.wang.gradf.component.business.event;

import liangchen.wang.gradf.component.business.manager.domain.parameter.NotificationParameterDomain;
import org.springframework.context.ApplicationEvent;

/**
 * @author LiangChen.Wang
 */
public class NotificationEvent extends ApplicationEvent {
    private final NotificationParameterDomain parameter;
    public NotificationEvent(Object source, NotificationParameterDomain parameter) {
        super(source);
        this.parameter = parameter;
    }

    public NotificationParameterDomain getParameter() {
        return parameter;
    }
}
