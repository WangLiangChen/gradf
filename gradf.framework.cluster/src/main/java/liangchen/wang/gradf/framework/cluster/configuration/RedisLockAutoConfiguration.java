package liangchen.wang.gradf.framework.cluster.configuration;

import liangchen.wang.gradf.framework.cluster.shedlock.RedisLockProvider;
import net.javacrumbs.shedlock.core.LockProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @author LiangChen.Wang
 */
public class RedisLockAutoConfiguration {
    @Bean
    public LockProvider jdbcTemplateLockProvider(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockProvider(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "GradfRedisLock");
    }
}
