package liangchen.wang.gradf.component.foura.spring.listener;

import liangchen.wang.gradf.component.foura.manager.IInitializationManager;
import liangchen.wang.gradf.framework.springboot.context.BeanLoader;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 4a初始化
 *
 * @author LiangChen.Wang
 */
public class FouraApplicationListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationReadyEvent) {
            this.onApplicationReady((ApplicationReadyEvent) applicationEvent);
        }
    }

    private void onApplicationReady(ApplicationReadyEvent applicationEvent) {
        ConfigurableApplicationContext applicationContext = applicationEvent.getApplicationContext();
        if (!(applicationContext instanceof AnnotationConfigServletWebServerApplicationContext)) {
            return;
        }
        IInitializationManager initializer = BeanLoader.getBean("Gradf_Foura_DefaultInitializationService", IInitializationManager.class);
        // 初始化数据和权限
        initializer.initData();
        initializer.initAuth();
    }

}
