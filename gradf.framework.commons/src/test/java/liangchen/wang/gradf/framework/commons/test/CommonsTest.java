package liangchen.wang.gradf.framework.commons.test;

import com.google.common.base.Splitter;
import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;
import liangchen.wang.gradf.framework.commons.utils.NetUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonsTest {
    Pattern p = Pattern.compile("[^0-9]");

    @Test
    public void testExtractNumbers() {
        System.out.println(StringUtil.INSTANCE.extractNumbers("\tupdates.20201027.1525.gz"));
    }

    @Test
    public void testUTC() {
        LocalDate localDate = LocalDate.now();
        localDate = DateTimeUtil.INSTANCE.utcDate(localDate);
        System.out.println(localDate);
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = DateTimeUtil.INSTANCE.utcDateTime(localDateTime);
        System.out.println(localDateTime);
    }

    @Test
    public void testIp2Int() {
        String ip = "1.0.0.0";
        long longIp = NetUtil.INSTANCE.ipV4ToLong(ip);
        System.out.println(longIp);
        ip = NetUtil.INSTANCE.longToIpV4(longIp);
        System.out.println(ip);

        ip = "255.255.255.255";
        longIp = NetUtil.INSTANCE.ipV4ToLong(ip);
        System.out.println(longIp);
        ip = NetUtil.INSTANCE.longToIpV4(longIp);
        System.out.println(ip);

        ip = "1.0.0.0/24";
        longIp = NetUtil.INSTANCE.ipV4ToLong(ip);
        System.out.println(longIp);
        ip = NetUtil.INSTANCE.longToIpV4(longIp);
        System.out.println(ip);

        ip = "255.255.255.255/23";
        longIp = NetUtil.INSTANCE.ipV4ToLong(ip);
        System.out.println(longIp);
        ip = NetUtil.INSTANCE.longToIpV4(longIp);
        System.out.println(ip);

        System.out.println("integer max:" + Integer.MAX_VALUE);
        System.out.println("long max:" + Long.MAX_VALUE);
        /*16777216
        1.0.0.0
        4294967295
        255.255.255.255*/
    }

}
