package liangchen.wang.gradf.framework.commons.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RetryInterceptor implements Interceptor {
    private byte maxRetry;
    private long retryInterval;

    public RetryInterceptor(Builder builder) {
        this.maxRetry = builder.maxRetry;
        this.retryInterval = builder.retryInterval;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        int retryNumber = 0;
        Request request = chain.request();
        Response response = proceed(chain, request);
        while ((null == response || !response.isSuccessful()) && retryNumber < maxRetry) {
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            retryNumber++;
            response = proceed(chain, request);
        }
        return response;
    }

    private Response proceed(Chain chain, Request request) {
        try {
            return chain.proceed(request);
        } catch (Exception e) {
            return null;
        }
    }

    public static final class Builder {
        private byte maxRetry;
        private long retryInterval;

        public Builder() {
            maxRetry = 3;
            retryInterval = 1000;
        }

        public Builder maxRetry(byte maxRetry) {
            this.maxRetry = maxRetry;
            return this;
        }

        public Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public RetryInterceptor build() {
            return new RetryInterceptor(this);
        }
    }

}
