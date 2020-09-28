package liangchen.wang.gradf.component.foura.configuration;

import liangchen.wang.gradf.component.foura.shiro.authc.ModularRealmAuthenticator;
import liangchen.wang.gradf.component.foura.shiro.filter.DefaultShiroFilterFactoryBean;
import liangchen.wang.gradf.component.foura.shiro.filter.LoginAuthorizationFilter;
import liangchen.wang.gradf.component.foura.shiro.filter.PermissionsAuthorizationFilter;
import liangchen.wang.gradf.component.foura.shiro.filter.RolesAuthorizationFilter;
import liangchen.wang.gradf.component.foura.shiro.filterchain.DefaultFilterChainManager;
import liangchen.wang.gradf.component.foura.shiro.filterchain.PathMatchingFilterChainResolver;
import liangchen.wang.gradf.component.foura.shiro.permission.BitAndWildPermissionResolver;
import liangchen.wang.gradf.component.foura.shiro.permission.DefaultRolePermissionResolver;
import liangchen.wang.gradf.component.foura.shiro.realm.*;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author LiangChen.Wang
 * 一、ShiroFilterFactoryBean -> SecurityManager -> Realm -> **Service，**Service事务等注解无效；因为ShiroFilterFactoryBean
 * 实例化早于处理动态代理等的BeanPostProcessor
 * 二、解决办法 **Service使用懒加载@Lazy
 * 三、为什么ShiroFilterFactoryBean会实例化的那么早
 * 1、在各种BeanPostProcessor实例化时，依赖注入根据类型获得需要注入的Bean时，会在类型判断匹配时(BeanFactory.isTryMatch)
 * 将FactoryBean类型实例化(前提时FactoryBean所在的FactoryBeanFactory(也就是Configuration)已经实例化)
 * 2、为什么Configuration会提前实例化，是因为里面的@Bean包含BeanPostProcessor或者BeanFactoryPostProcessor
 * <p>
 * 在AnnotationAwareAspectJAutoProxyCreator之前有一个MethodValidationPostProcessor被注册
 * 在注册过程中，需要传入Environment对象作为参数，然后Spring会从BeanFactory中查找所有符合Environment类型的bean
 * 在查找过程中，会实例化FactoryBean，导致ShiroFilterFactoryBean在注册processor阶段就被实例化了
 */
@Configuration
@AutoConfigureAfter(ShiroLifecycleConfiguration.class)
public class ShiroAutoConfiguration {
    private final static org.apache.commons.configuration2.Configuration config = ConfigurationUtil.INSTANCE.getConfiguration("4a.properties");
    private final CacheManager cacheManager;

    @Inject
    public ShiroAutoConfiguration(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    // 相当于调用SecurityUtils.setSecurityManager(securityManager)
    //@Bean
    @Deprecated
    public MethodInvokingFactoryBean securityUtils(DefaultWebSecurityManager securityManager) {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(securityManager);
        return factoryBean;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager, PathMatchingFilterChainResolver filterChainResolver) {
        DefaultShiroFilterFactoryBean shiroFilterFactoryBean = new DefaultShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilterChainResolver(filterChainResolver);
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //认证器
        securityManager.setAuthenticator(new ModularRealmAuthenticator());
        //授权器
        securityManager.setAuthorizer(authorizer());
        //realms
        Collection<Realm> realms = new HashSet<>();
        realms.add(accountPasswordRealm());
        realms.add(statelessRealm());
        realms.add(mobileCaptchaRealm());
        securityManager.setRealms(realms);

        boolean stateless = config.getBoolean("stateless", true);
        securityManager.setSubjectFactory(new DefaultWebSubjectFactory() {
            @Override
            public Subject createSubject(SubjectContext context) {
                if (stateless) {
                    context.setSessionCreationEnabled(false);
                }
                return super.createSubject(context);
            }
        });
        if (stateless) {
            ((DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager.getSubjectDAO()).getSessionStorageEvaluator()).setSessionStorageEnabled(false);
            DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
            sessionManager.setSessionIdCookieEnabled(false);
            securityManager.setSessionManager(sessionManager);
        }
        //设置静态方法
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public FilterChainManager filterChainManager() {
        DefaultFilterChainManager defaultFilterChainManager = new DefaultFilterChainManager();
        defaultFilterChainManager.addFilter("login", new LoginAuthorizationFilter());
        defaultFilterChainManager.addFilter("roles", new RolesAuthorizationFilter());
        defaultFilterChainManager.addFilter("perms", new PermissionsAuthorizationFilter());
        return defaultFilterChainManager;
    }

    @Bean
    public PathMatchingFilterChainResolver filterChainResolver(FilterChainManager filterChainManager) {
        PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
        filterChainResolver.setFilterChainManager(filterChainManager);
        return filterChainResolver;
    }

    private CredentialsMatcher retryLimitHashedCredentialsMatcher() {
        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager);
        retryLimitHashedCredentialsMatcher.setHashAlgorithmName("md5");
        retryLimitHashedCredentialsMatcher.setHashIterations(2);
        retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return retryLimitHashedCredentialsMatcher;
    }

    private CredentialsMatcher mobileCaptchaCredentialsMatcher() {
        MobileCaptchaCredentialsMatcher mobileCaptchaCredentialsMatcher = new MobileCaptchaCredentialsMatcher();
        return mobileCaptchaCredentialsMatcher;
    }

    private AccountPasswordRealm accountPasswordRealm() {
        AccountPasswordRealm accountPasswordRealm = new AccountPasswordRealm();
        accountPasswordRealm.setCachingEnabled(false);
        accountPasswordRealm.setAuthenticationCachingEnabled(false);
        accountPasswordRealm.setAuthorizationCachingEnabled(false);
        //凭证匹配器
        accountPasswordRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        return accountPasswordRealm;
    }

    private MobileCaptchaRealm mobileCaptchaRealm() {
        MobileCaptchaRealm mobileCaptchaRealm = new MobileCaptchaRealm();
        mobileCaptchaRealm.setCachingEnabled(false);
        mobileCaptchaRealm.setAuthenticationCachingEnabled(false);
        mobileCaptchaRealm.setAuthorizationCachingEnabled(false);
        //凭证匹配器
        mobileCaptchaRealm.setCredentialsMatcher(mobileCaptchaCredentialsMatcher());
        return mobileCaptchaRealm;
    }

    private JwtRealm statelessRealm() {
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCachingEnabled(false);
        jwtRealm.setAuthenticationCachingEnabled(false);
        jwtRealm.setAuthorizationCachingEnabled(false);
        //凭证匹配器
        jwtRealm.setCredentialsMatcher(new AllowAllCredentialsMatcher());
        return jwtRealm;
    }

    private Authorizer authorizer() {
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new BitAndWildPermissionResolver());
        authorizer.setRolePermissionResolver(new DefaultRolePermissionResolver());
        return authorizer;
    }

}