package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.http.NetResponse;
import liangchen.wang.gradf.framework.commons.http.HttpUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liangchen.wang 2020/9/10
 */
public class HttpTest {

    @Test
    public void testDownload() {
        List<String> urlList = new ArrayList<>();
        urlList.add("http://data.ris.ripe.net/rrc00/2020.10/updates.20201021.0420.gz");
        urlList.add("http://data.ris.ripe.net/rrc00/2020.10/updates.20201021.0415.gz");
        urlList.add("http://data.ris.ripe.net/rrc00/2020.10/updates.20201021.0410.gz");
        urlList.add("http://data.ris.ripe.net/rrc00/2020.10/updates.20201021.0405.gz");
        urlList.add("http://data.ris.ripe.net/rrc00/2020.10/updates.20201021.0400.gz");

        urlList.forEach(e -> {
            HttpUtil.INSTANCE.download(e, new NetResponse() {
                @Override
                public void onResponse(InputStream inputStream)  {
                    try {
                        Files.copy(inputStream, Paths.get("d:\\", UUID.randomUUID().toString() + ".gz"));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        });

        ConcurrentUtil.INSTANCE.threadSleep(10, TimeUnit.MINUTES);
    }

    //utc提前10分钟5分钟间隔取整
    @Test
    public void testUTC() {
        LocalDateTime utcDateTime = DateTimeUtil.INSTANCE.utcDateTime();
        utcDateTime = utcDateTime.minusMinutes(10);
        int minute = utcDateTime.getMinute();
        int tens = minute / 10 * 10;
        int ones = minute % 10 / 5 * 5;
        minute = tens + ones;
        utcDateTime = utcDateTime.withMinute(minute);
        System.out.println(LocalDateTime.now());
        System.out.println(utcDateTime);
    }
    //0000 0800 1600



}
