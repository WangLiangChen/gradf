package liangchen.wang.gradf.component.foura.configuration;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author LiangChen.Wang
 */
@Configuration
@Order
public class ShiroLifecycleConfiguration {
    // LifecycleBeanPostProcessor 是 BeanPostProcessor 的一个实现
    // 如果配置类中出现 BeanPostProcessor ，会破坏默认的 post-processing，将所在的Configuration提前实例化
    // 进而使里面的FactoryBean类型实例化，从而引发一连串的提前实例化，造成动态代理等功能失效
    // 此时Configuration不能初始化太早，spring建议使用 static
    // https://docs.spring.io/spring/docs/5.2.3.BUILD-SNAPSHOT/spring-framework-reference/core.html#beans
    // Also, be particularly careful with BeanPostProcessor and BeanFactoryPostProcessor definitions through @Bean....
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor(Ordered.LOWEST_PRECEDENCE);
    }
}