package liangchen.wang.gradf.framework.data.datasource;

import liangchen.wang.gradf.framework.commons.logger.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author LiangChen.Wang
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DynamicDataSourceContext.INSTANCE.get();
        if (null == dataSourceName || dataSourceName.length() == 0) {
            dataSourceName = "primary";
            DynamicDataSourceContext.INSTANCE.set(dataSourceName);
        }
        logger.debug("Current DataSource: {}", dataSourceName);
        return dataSourceName;
    }
}
