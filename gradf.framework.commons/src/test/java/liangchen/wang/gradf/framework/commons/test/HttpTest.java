package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.http.HttpResponse;
import liangchen.wang.gradf.framework.commons.http.HttpUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liangchen.wang 2020/9/10
 */
public class HttpTest {

    @Test
    public void testDownload() {
        HttpUtil.INSTANCE.download("https://onlinedown.rbread04.cn/huajunsafe/winhex.zip", new HttpResponse() {
            @Override
            public void onResponse(InputStream inputStream) throws IOException {
                Files.copy(inputStream, Paths.get("d://", UUID.randomUUID().toString() + ".zip"));
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        System.out.println("------------------========");
        ConcurrentUtil.INSTANCE.threadSleep(5, TimeUnit.MINUTES);
    }


}
