package liangchen.wang.gradf.framework.springboot.processor;

import liangchen.wang.gradf.framework.commons.utils.Printer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.PriorityOrdered;

/**
 * 该类会特别早的执行
 * Bean动态注册
 * Spring管理bean的对象是BeanFactory，具体的是DefaultListableBeanFactory
 * 在这个类当中有一个注入bean的方法：registerBeanDefinition
 * 在调用registerBeanDefinition方法时，需要BeanDefinition参数
 * Spring提供了BeanDefinitionBuilder可以构建一个BeanDefinition
 * 只要获取到ApplicationContext对象即可获取到BeanFacory
 *
 * @author LiangChen.Wang 2020/9/15
 */
public class FirstBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Printer.INSTANCE.prettyPrint("postProcessBeanDefinitionRegistry");
        //BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(XXX.class, () -> {return XXX});
        //设置依赖
        //beanDefinitionBuilder.addPropertyReference("personDao", "personDao");
        //BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        //注册bean定义
        //beanDefinitionRegistry.registerBeanDefinition("xxx", beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Printer.INSTANCE.prettyPrint("postProcessBeanFactory");
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }
}
