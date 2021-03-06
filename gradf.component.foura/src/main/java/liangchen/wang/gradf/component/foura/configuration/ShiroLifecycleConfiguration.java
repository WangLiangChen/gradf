package liangchen.wang.gradf.component.foura.configuration;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author LiangChen.Wang
 */

@Configuration(proxyBeanMethods = false)
public class ShiroLifecycleConfiguration {
    /**
     * LifecycleBeanPostProcessor 是 BeanPostProcessor 的一个实现
     * 如果配置类中出现 BeanPostProcessor ，会破坏默认的 post-processing，将所在的Configuration提前实例化
     * 进而使里面的FactoryBean类型实例化，从而引发一连串的提前实例化，造成动态代理等功能失效     *
     *
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor(Ordered.LOWEST_PRECEDENCE);
    }
}