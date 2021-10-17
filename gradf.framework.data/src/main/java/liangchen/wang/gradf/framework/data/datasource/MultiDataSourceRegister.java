package liangchen.wang.gradf.framework.data.datasource;


import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author LiangChen.Wang
 * 所有实现了该接口的类的都会被ConfigurationClassPostProcessor implement BeanFactoryPostProcessor处理
 * 所以这里注册的bean的初始化优先级较高，能被aop、validator等处理
 * 只能供给@Import注解或者是ImportSelector接口返回值
 */
public class MultiDataSourceRegister implements ImportBeanDefinitionRegistrar {
    // private final String DEFAULT_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";
    private final String DATASOURCE_BEAN_NAME = "dataSource";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        // 创建动态数据源
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MultiDataSource.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanDefinition.setBeanClass(MultiDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue("defaultTargetDataSource", MultiDataSourceContext.INSTANCE.getPrimaryDataSource());
        propertyValues.addPropertyValue("targetDataSources", MultiDataSourceContext.INSTANCE.getSecondaryDataSources());
        if (registry.containsBeanDefinition(DATASOURCE_BEAN_NAME)) {
            registry.removeBeanDefinition(DATASOURCE_BEAN_NAME);
        }
        registry.registerBeanDefinition(DATASOURCE_BEAN_NAME, beanDefinition);
    }
}
