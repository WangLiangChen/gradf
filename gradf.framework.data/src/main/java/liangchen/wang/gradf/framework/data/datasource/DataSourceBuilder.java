package liangchen.wang.gradf.framework.data.datasource;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <T>
 * @author LiangChen.Wang
 */
public final class DataSourceBuilder<T extends DataSource> {

    private static final String[] DATA_SOURCE_TYPE_NAMES = new String[]{"com.zaxxer.hikari.HikariDataSource",
            "org.apache.tomcat.jdbc.pool.DataSource",
            "org.apache.commons.dbcp2.BasicDataSource"};

    private Class<? extends DataSource> type;

    private ClassLoader classLoader;

    private Map<String, String> properties = new HashMap<>();

    public static DataSourceBuilder<?> create() {
        return new DataSourceBuilder<>(null);
    }

    public static DataSourceBuilder<?> create(ClassLoader classLoader) {
        return new DataSourceBuilder<>(classLoader);
    }

    private DataSourceBuilder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        Class<? extends DataSource> type = getType();
        DataSource result = BeanUtils.instantiateClass(type);
        maybeGetDriverClassName();
        bind(result);
        return (T) result;
    }

    private void maybeGetDriverClassName() {
        if (!this.properties.containsKey("driverClassName") && this.properties.containsKey("url")) {
            String url = this.properties.get("url");
            String driverClass = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
            this.properties.put("driverClassName", driverClass);
        }
    }

    private void bind(DataSource result) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(this.properties);
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases("driver-class-name", "driver-class");
        aliases.addAliases("url", "jdbc-url");
        aliases.addAliases("username", "user");
        Binder binder = new Binder(source.withAliases(aliases));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    @SuppressWarnings("unchecked")
    public <T extends DataSource> DataSourceBuilder<T> type(Class<T> type) {
        this.type = type;
        return (DataSourceBuilder<T>) this;
    }

    public DataSourceBuilder<T> url(String url) {
        this.properties.put("url", url);
        return this;
    }

    public DataSourceBuilder<T> driverClassName(String driverClassName) {
        this.properties.put("driverClassName", driverClassName);
        return this;
    }

    public DataSourceBuilder<T> username(String username) {
        this.properties.put("username", username);
        return this;
    }

    public DataSourceBuilder<T> password(String password) {
        this.properties.put("password", password);
        return this;
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends DataSource> findType(ClassLoader classLoader) {
        for (String name : DATA_SOURCE_TYPE_NAMES) {
            try {
                return (Class<? extends DataSource>) ClassUtils.forName(name, classLoader);
            } catch (Exception ex) {
                throw new ErrorException(ex);
            }
        }
        return null;
    }

    // 扩展增增加额外属性的方法，by liangchen.wang
    public DataSourceBuilder<T> property(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    // 扩展增增加额外属性的方法，by liangchen.wang
    public DataSourceBuilder<T> properties(Map<String, String> properties) {
        this.properties.putAll(properties);
        return this;
    }

    private Class<? extends DataSource> getType() {
        Class<? extends DataSource> type = (this.type != null) ? this.type : findType(this.classLoader);
        if (type != null) {
            return type;
        }
        throw new IllegalStateException("No supported DataSource type found");
    }

}
