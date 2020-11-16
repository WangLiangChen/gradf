package liangchen.wang.gradf.framework.commons.http;

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
        Request request = requestBuilder.get().url(url).addHeader("Connection", "close").build();
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

    public long contendLength(String url) {
        Request request = requestBuilder.get().url(url).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {

        }
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
