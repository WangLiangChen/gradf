package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.http.FtpUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author liangchen.wang 2020/9/10
 */
public class FtpTest {

    @Test
    public void testDownload() {

        ConcurrentUtil.INSTANCE.threadSleep(10, TimeUnit.MINUTES);
    }
    @Test
    public void testList(){
        FTPFile[] ftpFiles = FtpUtil.INSTANCE.listFiles("ftp://ftp.apnic.net/pub/apnic/");
        Arrays.stream(ftpFiles).map(FTPFile::getName).forEach(System.out::println);
    }

}
