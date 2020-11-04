package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.utils.CompressUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import liangchen.wang.gradf.framework.commons.http.FtpUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author liangchen.wang 2020/9/10
 */
public class FtpTest {

    @Test
    public void testDownload() {

        ConcurrentUtil.INSTANCE.threadSleep(10, TimeUnit.MINUTES);
    }

}
