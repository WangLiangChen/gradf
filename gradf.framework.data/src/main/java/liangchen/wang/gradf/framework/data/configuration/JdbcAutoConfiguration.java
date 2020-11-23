package liangchen.wang.gradf.framework.data.configuration;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.data.base.IAbstractDao;
import liangchen.wang.gradf.framework.data.mybatis.interceptor.PaginationInterceptor;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author LiangChen.Wang
 */
public class JdbcAutoConfiguration {
    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private final static String SYSTEM_MAPPER_SCAN_PACKAGE = "liangchen.wang.gradf";
    private final static Configuration config = ConfigurationUtil.INSTANCE.getConfiguration("autoscan.properties");
    private static String scanPackages = config.getString("mybatis");

    static {
        if (StringUtil.INSTANCE.isBlank(scanPackages)) {
            scanPackages = SYSTEM_MAPPER_SCAN_PACKAGE;
            Printer.INSTANCE.prettyPrint("Mybatis Scan Packages:Blank");
        } else {
            scanPackages = SYSTEM_MAPPER_SCAN_PACKAGE + "," + scanPackages;
        }
    }

    @Inject
    public void initSQL(DataSource dataSource) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        try {
            Resource[] ddls = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat("ddl.sql"));
            for (Resource ddl : ddls) {
                databasePopulator.addScript(ddl);
                Printer.INSTANCE.prettyPrint("init ddl sql script:{}", ddl.toString());
            }
            Resource[] dmls = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat("dml.sql"));
            for (Resource dml : dmls) {
                databasePopulator.addScript(dml);
                Printer.INSTANCE.prettyPrint("init dml sql script:{}", dml.toString());
            }
        } catch (IOException e) {
            throw new ErrorException(e);
        }
        databasePopulator.setSqlScriptEncoding("UTF-8");
        databasePopulator.execute(dataSource);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, @Nullable Interceptor[] interceptors) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        URL url = ConfigurationUtil.INSTANCE.getFullUrl("mybatis-config.xml");
        Resource configLocation = new UrlResource(url);
        if (configLocation.exists()) {
            sqlSessionFactoryBean.setConfigLocation(configLocation);
        } else {
            Printer.INSTANCE.prettyPrint("can't find mybatis-config.xml,use default configuration");
        }

        Properties mybatisConfiguration = new Properties();
        mybatisConfiguration.put("cacheEnabled", false);
        mybatisConfiguration.put("localCacheScope", LocalCacheScope.STATEMENT.name());
        sqlSessionFactoryBean.setConfigurationProperties(mybatisConfiguration);

        String[] packages = scanPackages.split(",");
        Resource[] mapperLocations = new Resource[0];
        for (String pack : packages) {
            pack = pack.replace('.', '/');
            try {
                Resource[] mappers = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat(pack).concat("/**/*.mapper.xml"));
                mapperLocations = ArrayUtils.addAll(mapperLocations, mappers);
            } catch (IOException e) {
                throw new ErrorException(e);
            }
        }
        if (CollectionUtil.INSTANCE.isEmpty(mapperLocations)) {
            Printer.INSTANCE.prettyPrint("can't find mybatis mapper file,pattern is:/**/*.mapper.xml");
            return sqlSessionFactoryBean;
        }
        Printer.INSTANCE.prettyPrint("find mybatis mapper file:");
        for (Resource resource : mapperLocations) {
            System.out.println(String.format("    %s", resource.toString()));
        }
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
        //添加plugins interceptors
        if (CollectionUtil.INSTANCE.isNotEmpty(interceptors)) {
            sqlSessionFactoryBean.setPlugins(interceptors);
        }
        //sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
        return sqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean
    // MapperScannerConfigurer 是 BeanFactoryPostProcessor 的一个实现
    // 如果配置类中出现 BeanFactoryPostProcessor ，会破坏默认的 post-processing 。
    // 此时不能初始化太早，spring建议使用 static
    // https://docs.spring.io/spring/docs/5.2.3.BUILD-SNAPSHOT/spring-framework-reference/core.html#beans
    // Also, be particularly careful with BeanPostProcessor and BeanFactoryPostProcessor definitions through @Bean....
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setSqlSessionTemplateBeanName("sqlSessionTemplate");
        // 指定扫描的类型,只扫描有注解的接口
        // mapperScannerConfigurer.setMarkerInterface(IAbstractDao.class);
        mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        mapperScannerConfigurer.setBasePackage(scanPackages);
        Printer.INSTANCE.prettyPrint("Mybatis Scan Packages:{}", scanPackages);
        return mapperScannerConfigurer;
    }

    @Bean
    public Interceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
