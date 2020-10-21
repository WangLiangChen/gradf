package liangchen.wang.gradf.framework.commons.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public enum HttpUtil {
    //
    INSTANCE;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Request.Builder requestBuilder = new Request.Builder();

    HttpUtil() {
        httpClient.dispatcher().setMaxRequests(64);
        httpClient.dispatcher().setMaxRequestsPerHost(10);
    }

    public void download(String url, HttpResponse httpResponse) {
        Request request = requestBuilder.get().url(url).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpResponse.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                httpResponse.onResponse(inputStream);
            }
        });
    }
}
