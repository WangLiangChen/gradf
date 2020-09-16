package liangchen.wang.gradf.framework.data.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author LiangChen.Wang
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DynamicDataSourceContext.INSTANCE.get();
        if (null == dataSourceName || dataSourceName.length() == 0) {
            dataSourceName = "default";
            DynamicDataSourceContext.INSTANCE.set(dataSourceName);
        }
        return dataSourceName;
    }
}
