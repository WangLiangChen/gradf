package liangchen.wang.gradf.framework.web.controller;

/**
 * @author LiangChen.Wang 2020/9/16
 */

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在WebMvc模式下使用异步WebClient
 */
public class WebmvcController {
    private final WebClient webClient;

    @Inject
    public WebmvcController(WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("unchecked")
    private Mono<Map<String, String>> resultByUrls(List<String> urls) {
        List<Mono<AbstractMap.SimpleEntry<String, String>>> res = urls.stream().map(url -> webClient.get().uri(url).retrieve().bodyToMono(String.class).map(s -> new AbstractMap.SimpleEntry<>(url, s))).collect(Collectors.toList());
        Mono<Map<String, String>> endRes = Mono.zip(res, objectArray -> Arrays.stream(objectArray).map(s -> (AbstractMap.SimpleEntry<String, String>) s).collect(Collectors.toMap(k -> k.getKey(), k -> k.getValue())));
        return endRes;
    }
}
