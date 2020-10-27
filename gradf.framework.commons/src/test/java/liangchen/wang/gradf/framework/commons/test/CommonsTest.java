package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

}
