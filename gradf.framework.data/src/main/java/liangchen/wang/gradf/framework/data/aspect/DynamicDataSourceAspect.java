package liangchen.wang.gradf.framework.data.aspect;

import liangchen.wang.gradf.framework.data.datasource.DynamicDataSourceContext;
import liangchen.wang.gradf.framework.data.annotation.SwitchDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author LiangChen.Wang
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicDataSourceAspect {
    // 很奇怪的实现方式
    // 使用如下切入点，可以拦截类和方法，但无法完全绑定方法的变量
    // 经测试，后面的绑定会覆盖前面的绑定
    @Before("@within(switchDataSource) || @annotation(switchDataSource)")
    public void changeDataSource(JoinPoint point, SwitchDataSource switchDataSource) {
        if (null == switchDataSource) {
            Object target = point.getTarget();
            Class<?> clazz = target.getClass();
            switchDataSource = clazz.getAnnotation(SwitchDataSource.class);
        }
        String dataSourceName = switchDataSource.value();
        DynamicDataSourceContext.INSTANCE.set(dataSourceName);
    }

    @After("@annotation(liangchen.wang.gradf.framework.data.annotation.SwitchDataSource) || @within(liangchen.wang.gradf.framework.data.annotation.SwitchDataSource)")
    public void restoreDataSource() {
        DynamicDataSourceContext.INSTANCE.clear();
    }
}
