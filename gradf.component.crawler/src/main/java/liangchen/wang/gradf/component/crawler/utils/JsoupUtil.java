package liangchen.wang.gradf.component.crawler.utils;

import liangchen.wang.gradf.framework.commons.http.NetResponse;
import okhttp3.internal.Util;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public enum JsoupUtil {
    //
    INSTANCE;
    private final ExecutorService executorService;

    JsoupUtil() {
        // 核心线程数0,最大线程数Integer.MAX_VALUE,空闲线程超时时间60 SECONDS , 线程等待队列SynchronousQueue(容量为0的队列)
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), Util.threadFactory("JsoupThread", false));
    }

    public void crawl(String url, NetResponse<Document> netResponse) {
        executorService.execute(() -> {
            try {
                Connection connection = Jsoup.connect(url).timeout(3600000).maxBodySize(5242880);
                Document document = connection.get();
                netResponse.onResponse(document);
            } catch (Exception e) {
                netResponse.onFailure(e);
            }
        });
    }
}
