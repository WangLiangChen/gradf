package liangchen.wang.gradf.framework.data.test;

import liangchen.wang.gradf.framework.data.annotation.EnableJdbc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableJdbc
public class DataTest {
    @Inject
    private ISwitchService switchService;

    @Test
    public void testTransactional(){
        switchService.testTransactional();
    }

}