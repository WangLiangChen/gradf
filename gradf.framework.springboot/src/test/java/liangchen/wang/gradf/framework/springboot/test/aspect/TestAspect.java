package liangchen.wang.gradf.framework.springboot.test.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author LiangChen.Wang 2020/9/21
 */
@Component
@Aspect
public class TestAspect {

    @Pointcut("target(liangchen.wang.gradf.framework.springboot.test.aspect.ITestAspect)")
    public void pointcutAutoCacheable() {

    }

    @Around("pointcutAutoCacheable()")
    public Object aroundAutoCacheable(ProceedingJoinPoint point) throws Throwable {
        String name = point.getSignature().getName();
        System.out.println("============>" + name);
        return point.proceed();
    }
}
