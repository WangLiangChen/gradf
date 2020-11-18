package liangchen.wang.gradf.framework.springboot.test;

import liangchen.wang.gradf.framework.springboot.test.aspect.ChildTestAspect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

/**
 * @author LiangChen.Wang 2020/9/16
 */
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
