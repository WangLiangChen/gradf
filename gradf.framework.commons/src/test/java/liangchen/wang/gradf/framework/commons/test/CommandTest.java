package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.http.HttpResponse;
import liangchen.wang.gradf.framework.commons.http.HttpUtil;
import liangchen.wang.gradf.framework.commons.utils.CommandUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liangchen.wang 2020/9/10
 */
public class CommandTest {

    @Test
    public void testPing() throws UnsupportedEncodingException {
        String ping = CommandUtil.INSTANCE.executeWithResult("ping", "127.0.0.1");
        System.out.println(ping);
    }


}
