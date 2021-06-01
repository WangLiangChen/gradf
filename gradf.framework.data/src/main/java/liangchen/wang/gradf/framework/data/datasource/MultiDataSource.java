package liangchen.wang.gradf.framework.data.datasource;

import liangchen.wang.gradf.framework.commons.logger.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author LiangChen.Wang
 */
public class MultiDataSource extends AbstractRoutingDataSource {
    private final Logger logger = LoggerFactory.getLogger(MultiDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = MultiDataSourceContext.INSTANCE.get();
        if (null == dataSourceName || dataSourceName.length() == 0) {
            dataSourceName = "primary";
            MultiDataSourceContext.INSTANCE.set(dataSourceName);
        }
        logger.debug("Current DataSource: {}", dataSourceName);
        return dataSourceName;
    }
}
