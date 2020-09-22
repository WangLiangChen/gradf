package liangchen.wang.gradf.framework.springboot.test;

import liangchen.wang.gradf.framework.springboot.test.aspect.ChildTestAspect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootTest {
    @Inject
    private ChildTestAspect testAspect;

    @Test
    public void testInit() {

    }

    @Test
    public void testAspect() {
        testAspect.childMethod();
        testAspect.parentMethod();
        testAspect.interfaceMethod();
        testAspect.abstractMethod();
        testAspect.abstractMethodWithBody();
    }
}
