package liangchen.wang.gradf.framework.data.configuration;

import com.google.common.base.Splitter;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.advisor.MultiDataSourceBeanFactoryPointcutAdvisor;
import liangchen.wang.gradf.framework.data.annotation.DataSource;
import liangchen.wang.gradf.framework.data.datasource.MultiDataSourceContext;
import liangchen.wang.gradf.framework.data.mybatis.interceptor.PaginationInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
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
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.lang.Nullable;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * @author LiangChen.Wang
 */
public class JdbcAutoConfiguration {
    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * 注册切换数据源切面
     *
     * @return
     */
    @Bean
    public MultiDataSourceBeanFactoryPointcutAdvisor dynamicDataSourceBeanFactoryPointcutAdvisor() {
        MultiDataSourceBeanFactoryPointcutAdvisor advisor = new MultiDataSourceBeanFactoryPointcutAdvisor();
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        advisor.setAdvice((MethodInterceptor) methodInvocation -> {
            Method method = methodInvocation.getMethod();
            DataSource dataSource = method.getAnnotation(DataSource.class);
            if (null == dataSource) {
                dataSource = method.getDeclaringClass().getAnnotation(DataSource.class);
            }
            String dataSourceName = dataSource.value();
            Assert.INSTANCE.isTrue(MultiDataSourceContext.INSTANCE.getDataSourceNames().contains(dataSourceName), AssertLevel.INFO, "The annotated dataSource '{}' does not exist", dataSourceName);

            MultiDataSourceContext.INSTANCE.set(dataSourceName);
            try {
                return methodInvocation.proceed();
            } finally {
                MultiDataSourceContext.INSTANCE.clear();
            }
        });
        return advisor;
    }

    /**
     * @param dataSource DynamicDataSource
     */
    @Inject
    public void initSQL(javax.sql.DataSource dataSource) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        try {
            // ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders("")
            Resource[] ddls = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat("ddl.sql"));
            for (Resource ddl : ddls) {
                Printer.INSTANCE.prettyPrint("init ddl sql script:{}", ddl.toString());
                databasePopulator.addScript(ddl);
            }
            Resource[] dmls = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat("dml.sql"));
            for (Resource dml : dmls) {
                Printer.INSTANCE.prettyPrint("init dml sql script:{}", dml.toString());
                databasePopulator.addScript(dml);
            }
        } catch (IOException e) {
            throw new ErrorException(e);
        }
        databasePopulator.setSqlScriptEncoding("UTF-8");
        databasePopulator.execute(dataSource);
    }


    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }
}
