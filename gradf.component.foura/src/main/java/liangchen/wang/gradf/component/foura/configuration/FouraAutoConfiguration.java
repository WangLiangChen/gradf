package liangchen.wang.gradf.component.foura.configuration;


import liangchen.wang.gradf.component.foura.spring.interceptor.AccessTokenHandlerInterceptor;
import liangchen.wang.gradf.component.foura.spring.resolver.AccessTokenHandlerMethodArgumentResolver;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.web.enumeration.Constant;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

/**
 * @author LiangChen.Wang
 * DelegatingFilterProxy 一个filter的代理，可以通过spring容器来管理filter的生命周期
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(ShiroAutoConfiguration.class)
public class FouraAutoConfiguration implements WebMvcConfigurer {
    private final org.apache.commons.configuration2.Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("4a.properties");

    @Bean
    public FilterRegistrationBean<Filter> fouraFilterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值默认为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.addUrlPatterns(Constant.Path.AUTH.getPath("*"));
        boolean enableAuth = configuration.getBoolean("enableAuth", true);
        filterRegistration.setEnabled(enableAuth);
        // filterRegistration.setOrder();
        return filterRegistration;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AccessTokenHandlerMethodArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         *注意这里的path配置,是/auth/**，而不是/business/auth/**
         *因为Interceptor是SpringMVC的，映射路径是/business
         */
        registry.addInterceptor(new AccessTokenHandlerInterceptor()).addPathPatterns("/auth/**");
    }
}