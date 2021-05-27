package liangchen.wang.gradf.framework.data.configuration;

import liangchen.wang.gradf.framework.data.shedlock.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.core.LockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author LiangChen.Wang
 */
public class JdbcShedLockProviderAutoConfiguration {

    @Bean
    public LockProvider jdbcTemplateLockProvider(JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateLockProvider(jdbcTemplate);
    }
}
