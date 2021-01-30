package liangchen.wang.gradf.framework.web.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebHandler;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author LiangChen.Wang
 */
//不能与@EnableWebMvc同时用,The Java/XML config for Spring MVC and Spring WebFlux cannot both be enabled, e.g. via @EnableWebMvc and @EnableWebFlux, in the same application.
//@EnableWebFlux
public class WebFluxAutoConfiguration implements WebFluxConfigurer {
    @Bean
    public WebHandler webHandler(ApplicationContext applicationContext) {
        DispatcherHandler dispatcherHandler = new DispatcherHandler(applicationContext);
        return dispatcherHandler;
    }

    @Bean
    public Scheduler scheduler() {
        return Schedulers.boundedElastic();
    }
}
