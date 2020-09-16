package liangchen.wang.gradf.framework.data.configuration;

import liangchen.wang.crdf.framework.commons.utils.CollectionUtil;
import liangchen.wang.crdf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.crdf.framework.commons.utils.Printer;
import liangchen.wang.crdf.framework.commons.utils.StringUtil;
import liangchen.wang.crdf.framework.data.base.IAbstractDao;
import liangchen.wang.crdf.framework.data.mybatis.interceptor.PaginationInterceptor;
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
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.net.URL;
import java.util.Properties;

/**
 * @author LiangChen.Wang
 */
public class JdbcAutoConfiguration {
    private final static String SYSTEM_MAPPER_SCAN_PACKAGE = "liangchen.wang.crdf.framework.springboot.data";

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, @Nullable Interceptor[] interceptors) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        URL url = ConfigurationUtil.INSTANCE.getFullUrl("mybatis-config.xml");
        try {
            Resource configLocation = new UrlResource(url);
            sqlSessionFactoryBean.setConfigLocation(configLocation);
        } catch (Exception e) {
            Printer.INSTANCE.prettyPrint("can't find mybatis-config.xml,use default configuration");
        }
        Properties mybatisConfiguration = new Properties();
        mybatisConfiguration.put("cacheEnabled", false);
        mybatisConfiguration.put("localCacheScope", LocalCacheScope.STATEMENT.name());
        sqlSessionFactoryBean.setConfigurationProperties(mybatisConfiguration);

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocations = resourcePatternResolver.getResources("classpath*:**/*.mapper.xml");
        if (CollectionUtil.INSTANCE.isEmpty(mapperLocations)) {
            Printer.INSTANCE.prettyPrint("can't find mybatis mapper file,pattern is:**/*.mapper.xml");
            return sqlSessionFactoryBean;
        }
        Printer.INSTANCE.prettyPrint("find mybatis mapper file:");
        for (Resource resource : mapperLocations) {
            System.out.println(String.format("    %s", resource.getURL()));
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
        //指定扫描的类型
        mapperScannerConfigurer.setMarkerInterface(IAbstractDao.class);

        org.apache.commons.configuration2.Configuration config = ConfigurationUtil.INSTANCE.getConfiguration("autoscan.properties");
        String scanPackages = config.getString("mybatis");
        if (StringUtil.INSTANCE.isBlank(scanPackages)) {
            scanPackages = SYSTEM_MAPPER_SCAN_PACKAGE;
            Printer.INSTANCE.prettyPrint("Mybatis Scan Packages:Blank");
        } else {
            scanPackages = SYSTEM_MAPPER_SCAN_PACKAGE + "," + scanPackages;
        }
        mapperScannerConfigurer.setBasePackage(scanPackages);
        Printer.INSTANCE.prettyPrint("Mybatis Scan Packages:{}", scanPackages);
        return mapperScannerConfigurer;
    }

    @Bean
    public Interceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
