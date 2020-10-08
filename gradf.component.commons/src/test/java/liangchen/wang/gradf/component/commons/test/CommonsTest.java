package liangchen.wang.gradf.component.commons.test;

import liangchen.wang.gradf.framework.data.annotation.EnableJdbc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJdbc
public class CommonsTest {
    @Test
    public void testInitSql() {
        System.out.println();
    }
}
