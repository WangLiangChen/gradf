package liangchen.wang.gradf.framework.cluster.configuration;

import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.Iterator;

/**
 * @author LiangChen.Wang
 */
public class RedisAutoConfiguration extends org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration {
    //通过@Primary注解覆盖掉通过配置文件生成的RedisProperties
    @Bean
    @Primary
    public RedisProperties loadRedisProperties() {
        Printer.INSTANCE.prettyPrint("create primary 'RedisProperties' from 'redis.properties'");
        org.apache.commons.configuration2.Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("redis.properties");
        //动态绑定参数
        MapConfigurationPropertySource source = new MapConfigurationPropertySource();
        Iterator<String> keys = configuration.getKeys();
        keys.forEachRemaining(k -> source.put(k, configuration.getProperty(k)));
        Binder binder = new Binder(source);
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(RedisProperties.class)).get();
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "CrdfLock");
    }
}
