package liangchen.wang.gradf.framework.web.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import liangchen.wang.gradf.framework.commons.bytes.IOStreamUtil;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;
import liangchen.wang.gradf.framework.web.enumeration.Constant;
import liangchen.wang.gradf.framework.web.filter.RootFilter;
import liangchen.wang.gradf.framework.web.gateway.ApiServlet;
import liangchen.wang.gradf.framework.web.resolver.FormUrlEncodedResolver;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LiangChen.Wang
 * 注册Filter的三种方式
 * 1、使用@WebFilter、@WebServlet、@WebListener
 * 2、使用FilterRegistrationBean、ServletRegistrationBea
 * 3、使用@Bean自动添加，添加后默认的过滤路径为 /*，使用FilterRegistrationBean来进行Filter的注册，filterRegistration.setEnabled(false);，就可以取消Filter的自动注册行为。
 */
//@EnableWebMvc
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    //注册servlet，@WebServlet需要在Configuration类上@ServletComponentScan
    @Bean
    public ServletRegistrationBean<ApiServlet> apiServlet() {
        ServletRegistrationBean<ApiServlet> servletRegistration = new ServletRegistrationBean<>(new ApiServlet(), "/business/auth/gateway/*");
        servletRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return servletRegistration;
    }

    //注册filter,@WebFilter需要在Configuration类上@ServletComponentScan
    @Bean
    public FilterRegistrationBean<Filter> rootFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>(new RootFilter());
        filterFilterRegistrationBean.setEnabled(true);
        filterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterFilterRegistrationBean.addUrlPatterns(Constant.Path.ROOT.getPath("*"));
        return filterFilterRegistrationBean;
    }

    //注册listener，@WebListener需要在Configuration类上@ServletComponentScan
    //@Bean
    public ServletListenerRegistrationBean<ServletContextListener> servletContextListener() {
        return new ServletListenerRegistrationBean<>();
    }

    private HttpMessageConverter<?> jacksonConverter() {
        class CatchExceptionMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
            @Override
            public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
                try {
                    if (type.getTypeName().equals(String.class.getName())) {
                        return IOStreamUtil.INSTANCE.readString(inputMessage.getBody());
                    }
                    return super.read(type, contextClass, inputMessage);
                } catch (IOException e) {
                    throw new ErrorException(e);
                } catch (Exception e) {
                    throw new InfoException("JSON格式错误:{}", e.getMessage());
                }
            }
        }
        final MappingJackson2HttpMessageConverter httpMessageConverter = new CatchExceptionMappingJackson2HttpMessageConverter();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeUtil.dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeUtil.dateTimeFormatter));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.YYYY_MM_DD_HH_MM_SS));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(javaTimeModule);
        httpMessageConverter.setObjectMapper(objectMapper);
        return httpMessageConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(jacksonConverter());
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        DataSize dataSize = DataSize.ofMegabytes(20);
        factory.setMaxFileSize(dataSize);
        factory.setMaxRequestSize(dataSize);
        return factory.createMultipartConfig();
    }

    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration(DispatcherServlet dispatcherServlet, @Nullable MultipartConfigElement multipartConfigElement) {
        ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<>(dispatcherServlet, Constant.Path.MVC.getPath("*"));
        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        if (null != multipartConfigElement) {
            registration.setMultipartConfig(multipartConfigElement);
        }
        return registration;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(false);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
        httpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.add(httpMessageConverter);
        restTemplate.setMessageConverters(httpMessageConverters);
        return restTemplate;
    }

    @Bean
    public Void register(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        // 解析Content-Type为application/json的默认解析器
        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;
        // 解析Content-Type为application/x-www-form-urlencoded的默认解析器
        ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor = null;
        List<HandlerMethodArgumentResolver> finalArgumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        for (HandlerMethodArgumentResolver finalArgumentResolver : finalArgumentResolvers) {
            if (requestResponseBodyMethodProcessor != null && servletModelAttributeMethodProcessor != null) {
                break;
            }
            if (finalArgumentResolver instanceof RequestResponseBodyMethodProcessor) {
                requestResponseBodyMethodProcessor = (RequestResponseBodyMethodProcessor) finalArgumentResolver;
                continue;
            }
            if (finalArgumentResolver instanceof ServletModelAttributeMethodProcessor) {
                servletModelAttributeMethodProcessor = (ServletModelAttributeMethodProcessor) finalArgumentResolver;
            }
        }
        if (requestMappingHandlerAdapter == null || servletModelAttributeMethodProcessor == null) {
            return null;
        }
        FormUrlEncodedResolver formUrlEncodedResolver = new FormUrlEncodedResolver(requestResponseBodyMethodProcessor, servletModelAttributeMethodProcessor);
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>(finalArgumentResolvers.size() + 1);
        argumentResolvers.add(formUrlEncodedResolver);
        argumentResolvers.addAll(finalArgumentResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
        return null;
    }

    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return (factory -> {
            ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
            factory.addErrorPages(errorPage404);
        });
    }
}