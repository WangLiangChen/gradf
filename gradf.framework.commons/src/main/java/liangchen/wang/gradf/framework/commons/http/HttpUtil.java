package liangchen.wang.gradf.framework.commons.http;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public enum HttpUtil {
    //
    INSTANCE;
    private final OkHttpClient httpClient = new OkHttpClient().newBuilder().retryOnConnectionFailure(true)
            .connectTimeout(2, TimeUnit.MINUTES).readTimeout(10, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.MINUTES).build();
    private final Request.Builder requestBuilder = new Request.Builder();

    HttpUtil() {
        httpClient.dispatcher().setMaxRequests(64);
        httpClient.dispatcher().setMaxRequestsPerHost(10);
    }

    public void download(String url, NetResponse<InputStream> netResponse) {
        Request request = requestBuilder.get().url(url).addHeader("Connection", "close")
                .removeHeader("User-Agent").addHeader("User-Agent", UserAgent.byOsRandom(UserAgent.Os.Windows, UserAgent.Os.Linux, UserAgent.Os.Mac)).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                netResponse.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) {
                // 谁用谁关闭
                InputStream inputStream = response.body().byteStream();
                netResponse.onResponse(inputStream);
            }
        });
    }

    public long contentLength(String url) {
        Request request = requestBuilder.head().url(url).build();
        try {
            Response response = httpClient.newCall(request).execute();
            return response.headers().byteCount();
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

}
