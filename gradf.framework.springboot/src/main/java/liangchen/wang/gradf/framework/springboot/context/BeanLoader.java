package liangchen.wang.gradf.framework.springboot.context;

import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

/**
 * @author LiangChen.Wang
 */
public enum BeanLoader {
    // instance
    INSTANCE;
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public <T> T getBean(String name) {
        Object bean = this.applicationContext.getBean(name);
        return ClassBeanUtil.INSTANCE.cast(bean);
    }

    public <T> T getBean(Class<T> clazz) {
        return this.applicationContext.getBean(clazz);
    }

    public <T> T getBean(String name, Class<T> clazz) {
        return this.applicationContext.getBean(name, clazz);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return this.applicationContext.getBeansOfType(clazz);
    }

    public Class<?> getType(String name) {
        return this.applicationContext.getType(name);
    }

    public String[] getBeanDefinitionNames() {
        return this.applicationContext.getBeanDefinitionNames();
    }

    @SuppressWarnings("unchecked")
    public <T> T registerBean(String name, Class<T> clazz, Object... args) {
        if (applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new InfoException("Bean Name 重复");
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }

    public void removeBean(String beanName) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        beanFactory.removeBeanDefinition(beanName);
    }
}
