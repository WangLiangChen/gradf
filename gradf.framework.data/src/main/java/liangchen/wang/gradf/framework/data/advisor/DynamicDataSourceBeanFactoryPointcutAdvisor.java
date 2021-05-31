package liangchen.wang.gradf.framework.data.advisor;

import liangchen.wang.gradf.framework.data.annotation.DataSourceSwitchable;
import liangchen.wang.gradf.framework.data.annotation.SwitchDataSource;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

import java.lang.reflect.Method;

/**
 * @author LiangChen.Wang 2021/5/31
 */
public class DynamicDataSourceBeanFactoryPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

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
                        if (method.getAnnotation(SwitchDataSource.class) != null) {
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
