package liangchen.wang.gradf.framework.data.advisor;

import liangchen.wang.gradf.framework.data.annotation.DataSource;
import liangchen.wang.gradf.framework.data.annotation.DataSourceSwitchable;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

import java.lang.reflect.Method;

/**
 * @author LiangChen.Wang 2021/5/31
 * 采用切面的方式 使用注解切换数据源
 */
@SuppressWarnings("NullableProblems")
public class MultiDataSourceBeanFactoryPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    @Override
    public Pointcut getPointcut() {
        return new Pointcut() {
            @Override
            public MethodMatcher getMethodMatcher() {
                return new MethodMatcher() {
                    @Override
                    public boolean matches(Method method, Class<?> targetClass, Object[] args) {
                        return matches(method, targetClass);
                    }

                    @Override
                    public boolean matches(Method method, Class<?> targetClass) {
                        if (targetClass.getAnnotation(DataSource.class) != null) {
                            return true;
                        }
                        if (method.getAnnotation(DataSource.class) != null) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean isRuntime() {
                        return true;
                    }
                };
            }

            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getAnnotation(DataSourceSwitchable.class) != null ? true : false;
            }
        };

    }
}
