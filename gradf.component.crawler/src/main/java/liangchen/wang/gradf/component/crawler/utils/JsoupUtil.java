package liangchen.wang.gradf.component.crawler.utils;

import liangchen.wang.gradf.framework.commons.http.NetResponse;
import liangchen.wang.gradf.framework.commons.http.UserAgent;
import liangchen.wang.gradf.framework.commons.utils.ThreadPoolUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public enum JsoupUtil {
    //
    INSTANCE;

    public void crawl(String url, NetResponse<Document> netResponse) {
        ThreadPoolUtil.INSTANCE.getExecutor().execute(() -> {
            try {
                Connection connection = Jsoup.connect(url).timeout(3600000).maxBodySize(5242880)
                        .userAgent(UserAgent.byOsRandom(UserAgent.Os.Windows, UserAgent.Os.Linux, UserAgent.Os.Mac));
                Document document = connection.get();
                netResponse.onResponse(document);
            } catch (Exception e) {
                netResponse.onFailure(e);
            }
        });
    }
}
