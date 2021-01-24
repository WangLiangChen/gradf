package liangchen.wang.gradf.framework.data.datasource;


import liangchen.wang.gradf.framework.data.datasource.dialect.AbstractDialect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiangChen.Wang
 */
public enum DynamicDataSourceContext {
    /**
     *
     */
    INSTANCE;
    private final ThreadLocal<String> context = new ThreadLocal<>();
    private final Map<String, AbstractDialect> cachedDialect = new HashMap<>(10);

    public void putDialect(String dataSourceName, AbstractDialect dialect) {
        cachedDialect.put(dataSourceName, dialect);
    }

    public AbstractDialect getDialect() {
        return cachedDialect.get(get());
    }

    public void set(String dataSourceName) {
        context.set(dataSourceName);
    }

    public String get() {
        return context.get();
    }

    public void clear() {
        context.remove();
    }
}
