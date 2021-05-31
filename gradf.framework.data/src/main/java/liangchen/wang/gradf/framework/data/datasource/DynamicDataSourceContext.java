package liangchen.wang.gradf.framework.data.datasource;


import liangchen.wang.gradf.framework.data.datasource.dialect.AbstractDialect;

import javax.sql.DataSource;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LiangChen.Wang
 */
public enum DynamicDataSourceContext {
    /**
     *
     */
    INSTANCE;
    private final ThreadLocal<Deque<String>> context = ThreadLocal.withInitial(() -> new ArrayDeque<>());
    private final Map<String, AbstractDialect> cachedDialects = new LinkedHashMap<>();
    private final Map<String, DataSource> cachedDataSources = new LinkedHashMap<>();
    public final String PRIMARY_DATASOURCE_NAME = "primary";


    public void putDialect(String dataSourceName, AbstractDialect dialect) {
        cachedDialects.put(dataSourceName, dialect);
    }

    public void putDataSource(String dataSourceName, DataSource dataSource) {
        cachedDataSources.put(dataSourceName, dataSource);
    }

    public AbstractDialect getDialect(String dataSourceName) {
        return cachedDialects.get(dataSourceName);
    }

    public AbstractDialect getDialect() {
        return getDialect(get());
    }

    public DataSource getDataSource(String dataSourceName) {
        return cachedDataSources.get(dataSourceName);
    }

    public DataSource getPrimaryDataSource() {
        return cachedDataSources.get(PRIMARY_DATASOURCE_NAME);
    }

    public Map<String, DataSource> getSecondaryDataSources() {
        Map<String, DataSource> secondaryDataSources = new LinkedHashMap<>();
        cachedDataSources.forEach((dataSourceName, dataSource) -> {
            if (PRIMARY_DATASOURCE_NAME.equals(dataSourceName)) {
                return;
            }
            secondaryDataSources.put(dataSourceName, dataSource);
        });
        return secondaryDataSources;
    }

    public Map<String, DataSource> getDataSources() {
        return cachedDataSources;
    }

    public void set(String dataSourceName) {
        Deque<String> deque = context.get();
        if (null == deque) {
            deque = new ArrayDeque<>();
            context.set(deque);
        }
        deque.push(dataSourceName);
    }

    public String get() {
        Deque<String> deque = context.get();
        if (null == deque) {
            return null;
        }
        return deque.peek();
    }

    public void clear() {
        Deque<String> deque = context.get();
        if (null == deque) {
            return;
        }
        if (deque.isEmpty()) {
            context.remove();
            return;
        }
        deque.pop();
    }

}
