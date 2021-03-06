package liangchen.wang.gradf.framework.web.controller;

import liangchen.wang.gradf.framework.web.sse.SseKey;
import liangchen.wang.gradf.framework.web.sse.SseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.concurrent.Callable;

/**
 * Server Side Event
 *
 * @author LiangChen.Wang 2019/9/28 15:06
 * 异步异常拦截 CallableProcessingInterceptor/DeferredResultProcessingInterceptor/AsyncHandlerInterceptor
 */
@RestController
public class SseController {
    private DeferredResult<String> deferredResult = new DeferredResult<>();

    @GetMapping("/ssc")
    public SseEmitter sse(String name, String group) {
        return SseUtil.INSTANCE.buildEmitter(SseKey.newInstance(name, group));
    }

    @GetMapping("callable")
    public Callable callable() {
        Callable callable = () -> null;
        return callable;
    }

    @GetMapping("/deferredResult")
    public DeferredResult testDeferredResult() {
        // 客户端将会一直等待，直到一定时长后会超时或者其它线程调用deferredResult.setResult
        return deferredResult;
    }

    @GetMapping("/streamingResponseBody")
    public StreamingResponseBody streamingResponseBody() {
        // 用于直接将结果写出到Response的OutputStream中； 如文件下载等
        return outputStream -> {
        };
    }
}
