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
    /*
    MapperScannerConfigurer 是 BeanFactoryPostProcessor 的一个实现
    如果配置类中出现 BeanFactoryPostProcessor ，会破坏默认的 post-processing 。
    静态化从而不再需要依赖所在类的实例即可运行,防止因这个bean初始化早而导致 JdbcAutoConfiguration初始化过早
    static静态方法属于类，执行静态方法时并不需要初始化所在类的实例
    static关键字一般有且仅用于@Bean方法返回为BeanPostProcessor、BeanFactoryPostProcessor等类型的方法，并且建议此种方法请务必使用static修饰
    https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/core.html#beans
    Also, be particularly careful with BeanPostProcessor and BeanFactoryPostProcessor definitions through @Bean. Those should usually be declared as static @Bean methods, not triggering the instantiation of their containing configuration class. Otherwise, @Autowired and @Value do not work on the configuration class itself, since it is being created as a bean instance too early.
    另外，通过@Bean使用BeanPostProcessor和BeanFactoryPostProcessor定义时要特别小心。 通常应将这些声明为静态@Bean方法，而不触发其包含的配置类的实例化。 否则，@Autowired和@Value不适用于配置类本身，因为它太早被创建为Bean实例。
    */
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
