package liangchen.wang.gradf.framework.data.datasource;


import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

//所有实现了该接口的类的都会被ConfigurationClassPostProcessor处理，实现了BeanFactoryPostProcessor接口，
//所以ImportBeanDefinitionRegistrar中动态注册的bean是优先于依赖其的bean初始化的，也能被aop、validator等机制处理。
//只能供给@Import注解或者是ImportSelector接口返回值

/**
 * @author LiangChen.Wang
 */
public class DataSourceRegister implements ImportBeanDefinitionRegistrar {
    // private final String DEFAULT_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";
    private final String DATASOURCE_BEAN_NAME = "dataSource";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        // 创建动态数据源
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue("defaultTargetDataSource", DynamicDataSourceContext.INSTANCE.getPrimaryDataSource());
        propertyValues.addPropertyValue("targetDataSources", DynamicDataSourceContext.INSTANCE.getSecondaryDataSources());
        if (registry.containsBeanDefinition(DATASOURCE_BEAN_NAME)) {
            registry.removeBeanDefinition(DATASOURCE_BEAN_NAME);
        }
        registry.registerBeanDefinition(DATASOURCE_BEAN_NAME, beanDefinition);
    }
}
