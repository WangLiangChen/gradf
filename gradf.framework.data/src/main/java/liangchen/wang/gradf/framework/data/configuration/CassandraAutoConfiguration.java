package liangchen.wang.gradf.framework.data.configuration;

import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.apache.commons.configuration2.Configuration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

public class CassandraAutoConfiguration {
    private final String CASSANDRA_CONFIG_FILE = "cassandra.properties";

    @Bean
    public CassandraProperties cassandraProperties() {
        Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration(CASSANDRA_CONFIG_FILE);
        Map<String, Object> configItems = new HashMap<>();
        configuration.getKeys().forEachRemaining(key -> configItems.putIfAbsent(key, configuration.getProperty(key)));

        ConfigurationPropertySource source = new MapConfigurationPropertySource(configItems);
        Binder binder = new Binder(source);
        CassandraProperties cassandraProperties = new CassandraProperties();
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(cassandraProperties));
        return cassandraProperties;
    }
}
