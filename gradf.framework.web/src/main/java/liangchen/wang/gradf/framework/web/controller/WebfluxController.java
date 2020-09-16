package liangchen.wang.gradf.framework.web.controller;

/**
 * @author LiangChen.Wang 2020/9/16
 */

import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.inject.Inject;
import java.util.function.Function;

/**
 * 在WebFlux模式下使用同步restTemplate
 */
public class WebfluxController {
    private final RestTemplate restTemplate;
    private final Scheduler scheduler;

    @Inject
    public WebfluxController(RestTemplate restTemplate, Scheduler scheduler) {
        this.restTemplate = restTemplate;
        this.scheduler = scheduler;
    }

    /**
     * block(param,e->{});
     * block(Optional.empty(),e->{})
     *
     * @param params
     * @param call
     * @param <T>
     * @param <Y>
     * @return
     */
    private <T, Y> Mono<Y> block(T params, Function<T, ? extends Y> call) {
        return Mono.just(params).subscribeOn(scheduler).map(e -> call.apply(e));
    }
}