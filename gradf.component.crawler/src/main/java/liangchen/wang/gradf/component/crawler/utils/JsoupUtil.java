package liangchen.wang.gradf.component.crawler.utils;

import liangchen.wang.gradf.framework.commons.http.NetResponse;
import liangchen.wang.gradf.framework.commons.utils.ThreadPoolUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public enum JsoupUtil {
    //
    INSTANCE;

    public void crawl(String url, NetResponse<Document> netResponse) {
        ThreadPoolUtil.INSTANCE.getExecutorService().execute(() -> {
            try {
                Connection connection = Jsoup.connect(url).timeout(3600000).maxBodySize(5242880).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
                Document document = connection.get();
                netResponse.onResponse(document);
            } catch (Exception e) {
                netResponse.onFailure(e);
            }
        });
    }
}
