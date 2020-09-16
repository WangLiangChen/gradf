package liangchen.wang.gradf.framework.data.datasource;

import liangchen.wang.crdf.framework.commons.exeception.ErrorException;
import liangchen.wang.crdf.framework.commons.exeception.InfoException;
import liangchen.wang.crdf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.crdf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.crdf.framework.commons.utils.Printer;
import liangchen.wang.crdf.framework.commons.utils.StringUtil;
import liangchen.wang.crdf.framework.data.datasource.dialect.AbstractDialect;
import liangchen.wang.crdf.framework.data.datasource.dialect.MySQLDialect;
import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
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
    private static final String DEFAULT_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata meta, BeanDefinitionRegistry registry) {
        Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("jdbc.properties");
        Iterator<String> keys = configuration.getKeys();
        Map<String, Map<String, String>> properties = new HashMap<>();
        Map<String, String> subProperties;
        while (keys.hasNext()) {
            String key = keys.next();
            int split = key.indexOf(".");
            String prefix = key.substring(0, split);
            subProperties = properties.computeIfAbsent(prefix, k -> new HashMap<>());
            String subKey = key.substring(split + 1);
            subProperties.put(subKey, configuration.getString(key));
        }
        if (!properties.containsKey("default")) {
            throw new InfoException("default datasource is not exists");
        }
        // 创建数据源
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", buildDefaultTargetDatasource(properties));
        mpv.addPropertyValue("targetDataSources", buildTargetDataSources(properties));

        registry.registerBeanDefinition("dataSource", beanDefinition);
    }

    private DataSource buildDefaultTargetDatasource(Map<String, Map<String, String>> properties) {
        String dataSourceName = "default";
        Map<String, String> defaultDataSource = properties.get(dataSourceName);
        Printer.INSTANCE.prettyPrint("Init DataSource:default,properties:{}", defaultDataSource);
        DataSource dataSource = buildDataSource(dataSourceName, defaultDataSource);
        properties.remove(dataSourceName);
        return dataSource;
    }

    private Map<String, DataSource> buildTargetDataSources(Map<String, Map<String, String>> properties) {
        Map<String, DataSource> targetDataSources = new HashMap<>(properties.size(), 1);
        properties.forEach((k, v) -> {
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
            dataSourceType = ClassBeanUtil.INSTANCE.classCast(Class.forName(DEFAULT_DATASOURCE));
            // 先获取默认的类 如果下面的指定类出现异常 则使用默认的类
            dataSourceType = ClassBeanUtil.INSTANCE.classCast(Class.forName(datasource));
        } catch (ClassNotFoundException e) {
            Printer.INSTANCE.prettyPrint("DataSource Class Not Found:{}", datasource);
            Printer.INSTANCE.prettyPrint("use default DataSource:{}", DEFAULT_DATASOURCE);
        }
        String driverClassName = null;
        String url = null;
        if (dialect instanceof MySQLDialect) {
            driverClassName = "com.mysql.cj.jdbc.Driver";
            String query = "serverTimezone=GMT%2B8&characterEncoding=utf-8&characterSetResults=utf-8&useUnicode=false&useSSL=false&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true";
            url = String.format("jdbc:mysql://%s:%s/%s?%s", properties.get("host"), properties.get("port"), properties.get("database"), query);
            properties.remove("host");
            properties.remove("port");
            properties.remove("database");
        }
        DataSourceBuilder<DataSource> factory = DataSourceBuilder.create().type(dataSourceType).
                driverClassName(driverClassName).url(url).properties(properties);
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
