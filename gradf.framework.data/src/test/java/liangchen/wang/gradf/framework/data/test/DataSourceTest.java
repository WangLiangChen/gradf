package liangchen.wang.gradf.framework.data.test;

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
public class DataSourceTest {
    @Test
    public void testDataSourceProperties() {
        System.out.println();
    }
}
