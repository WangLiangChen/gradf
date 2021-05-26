package liangchen.wang.gradf.component.foura.test;

import liangchen.wang.gradf.component.foura.shiro.utils.ShiroUtil;
import liangchen.wang.gradf.framework.data.annotation.EnableJdbc;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableJdbc
@EnableCaching
public class ShiroTest {

    @Test
    public void testChain() {

    }
}
