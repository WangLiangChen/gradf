package liangchen.wang.gradf.framework.data.datasource;


import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.data.datasource.dialect.AbstractDialect;
import liangchen.wang.gradf.framework.data.datasource.dialect.MySQLDialect;
import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//所有实现了该接口的类的都会被ConfigurationClassPostProcessor处理，实现了BeanFactoryPostProcessor接口，
//所以ImportBeanDefinitionRegistrar中动态注册的bean是优先于依赖其的bean初始化的，也能被aop、validator等机制处理。
//只能供给@Import注解或者是ImportSelector接口返回值

/**
 * @author LiangChen.Wang
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar {
    private final String DEFAULT_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";
    private final Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("datasource.properties");

    @Override
    public void registerBeanDefinitions(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        // 将配置分组
        Iterator<String> keys = configuration.getKeys();
        Map<String, Map<String, String>> datasourceMap = new HashMap<>();
        keys.forEachRemaining(key -> {
            String[] array = key.split("\\.");
            Map<String, String> properties = datasourceMap.get(array[0]);
            if (null == properties) {
                properties = new HashMap<>();
                datasourceMap.put(array[0], properties);
            }
            properties.put(array[1], configuration.getString(key));
        });

        // 创建数据源
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue("defaultTargetDataSource", buildDefaultTargetDatasource(datasourceMap));
        propertyValues.addPropertyValue("targetDataSources", buildTargetDataSources(datasourceMap));

        registry.registerBeanDefinition("dataSource", beanDefinition);
    }

    private DataSource buildDefaultTargetDatasource(Map<String, Map<String, String>> datasourceMap) {
        String dataSourceName = "default";
        Map<String, String> defaultDataSource = datasourceMap.get(dataSourceName);
        Printer.INSTANCE.prettyPrint("Init DataSource:default,properties:{}", defaultDataSource);
        DataSource dataSource = buildDataSource(dataSourceName, defaultDataSource);
        datasourceMap.remove(dataSourceName);
        return dataSource;
    }

    private Map<String, DataSource> buildTargetDataSources(Map<String, Map<String, String>> datasourceMap) {
        Map<String, DataSource> targetDataSources = new HashMap<>(datasourceMap.size(), 1);
        datasourceMap.forEach((k, v) -> {
            Printer.INSTANCE.prettyPrint("Init DataSource:{},properties:{}", k, v);
            DataSource buildDataSource = buildDataSource(k, v);
            targetDataSources.put(k, buildDataSource);
        });
        return targetDataSources;
    }

    private DataSource buildDataSource(String dataSourceName, Map<String, String> properties) {
        // 创建dialect
        AbstractDialect dialect = createDialect(dataSourceName, properties.get("dialect"));
        properties.remove("dialect");
        String datasource = properties.get("datasource");
        properties.remove("datasource");
        if (StringUtil.INSTANCE.isBlank(datasource)) {
            datasource = DEFAULT_DATASOURCE;
            Printer.INSTANCE.prettyPrint("Use default DataSource:{}", DEFAULT_DATASOURCE);
        } else {
            Printer.INSTANCE.prettyPrint("Use DataSource:{}", datasource);
        }
        Class<DataSource> dataSourceType = null;
        try {
            dataSourceType = ClassBeanUtil.INSTANCE.cast(Class.forName(DEFAULT_DATASOURCE));
            // 先获取默认的类 如果下面的指定类出现异常 则使用默认的类
            dataSourceType = ClassBeanUtil.INSTANCE.cast(Class.forName(datasource));
        } catch (ClassNotFoundException e) {
            Printer.INSTANCE.prettyPrint("DataSource Class Not Found:{},use default DataSource:{}", datasource, DEFAULT_DATASOURCE);
        }
        String driverClassName = null;
        String url = null;
        if (dialect instanceof MySQLDialect) {
            driverClassName = "com.mysql.cj.jdbc.Driver";
            String query = "serverTimezone=GMT%2B8&characterEncoding=utf-8&characterSetResults=utf-8&useUnicode=true&useSSL=false&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true";
            url = String.format("jdbc:mysql://%s:%s/%s?%s", properties.get("host"), properties.get("port"), properties.get("database"), query);
            properties.remove("host");
            properties.remove("port");
            properties.remove("database");
        }
        DataSourceBuilder<DataSource> factory = DataSourceBuilder.create().type(dataSourceType).driverClassName(driverClassName).url(url).properties(properties);
        return factory.build();
    }

    //创建自定义dialect
    private AbstractDialect createDialect(String dataSourceName, String dialectClass) {
        try {
            Class<?> forName = Class.forName(dialectClass);
            AbstractDialect dialect = (AbstractDialect) forName.getDeclaredConstructor().newInstance();
            DynamicDataSourceContext.INSTANCE.putDialect(dataSourceName, dialect);
            return dialect;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ErrorException(e.toString());
        }
    }

}
