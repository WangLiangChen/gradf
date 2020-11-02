package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.utils.CompressUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import liangchen.wang.gradf.framework.commons.utils.FtpUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * @author liangchen.wang 2020/9/10
 */
public class FtpTest {

    @Test
    public void testDownload() {

//ftp://ftp.apnic.net/pub/apnic/whois/apnic.db.rtr-set.gz
        FtpUtil.INSTANCE.download("ftp.apnic.net", 21, "/pub/apnic/whois/", "apnic.db.rtr-set.gz", inputStream -> {
            String s = CompressUtil.INSTANCE.gzDecompress(inputStream);
            System.out.println("-------------");
            System.out.println(s);
        });

        ConcurrentUtil.INSTANCE.threadSleep(10, TimeUnit.MINUTES);
    }

}
